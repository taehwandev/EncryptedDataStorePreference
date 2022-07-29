package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.visitor

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.filterAnnotation
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.findDisableEncrypted
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.findKeyArgument
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnElement
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnResolve
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.hasSuspend
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ValueModel
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.writeLogger

/**
 * Research Annotation and struct.
 */
internal fun Resolver.findUsefulPreferences(
    @Suppress("UNUSED_PARAMETER") logger: KSPLogger,
): List<ResearchModel> =
    getSymbolsWithAnnotation(DataStoreConst.ANNOTATION_USEFUL_PREFERENCES.canonicalName)
        .filter { ksAnnotated ->
            ksAnnotated is KSClassDeclaration
        }
        .map { ksAnnotated ->
            ksAnnotated as KSClassDeclaration
        }
        .map { classDeclaration ->
            val declaredFunctions = classDeclaration.getDeclaredFunctions()
            val disableSecurity = classDeclaration.findDisableEncrypted()

            val findDeclaredFunctions = declaredFunctions
                .filter { functionDeclaration ->
                    functionDeclaration.filterAnnotation {
                        it == DataStoreConst.ANNOTATION_GET_VALUE.simpleName || it == DataStoreConst.ANNOTATION_SET_VALUE.simpleName
                    }
                }
                .mapNotNull { functionDeclaration ->
                    functionDeclaration
                        .findKeyArgument()?.let { key ->
                            when (functionDeclaration.annotations.first().shortName.asString()) {
                                DataStoreConst.ANNOTATION_GET_VALUE.simpleName -> {
                                    // First, the return type is found in the element.
                                    // If there is no information, a non generic type is searched through resolve().
                                    (functionDeclaration.getReturnElement()?.simpleName
                                        ?: functionDeclaration.getReturnResolve()?.simpleName)?.let { returnType ->
                                        ValueModel.Get(
                                            key = key,
                                            functionInfo = functionDeclaration,
                                            valueType = returnType,
                                            isSuspend = functionDeclaration.modifiers.hasSuspend(),
                                        )
                                    }
                                }
                                else -> {
                                    functionDeclaration.parameters.firstOrNull()?.type?.resolve()?.declaration?.qualifiedName?.let { parameters ->
                                        ValueModel.Set(
                                            key = key,
                                            functionInfo = functionDeclaration,
                                            isSuspend = functionDeclaration.modifiers.hasSuspend(),
                                            valueType = parameters,
                                        )
                                    }
                                }
                            }
                        }
                }
                .onEach {
                    logger.writeLogger("------ Find new key info : ${it.key}")
                    logger.writeLogger("function name : ${it.functionInfo.simpleName.asString()}")
                    it.functionInfo.modifiers.forEach {
                        logger.writeLogger("function modifier : ${it.name}")
                    }
                    logger.writeLogger("return type default : ${it.functionInfo.getReturnResolve()?.simpleName?.asString()}")
                    logger.writeLogger("return type full name : ${it.functionInfo.getReturnResolve()?.qualifiedName?.asString()}")
                    logger.writeLogger("return type generic : ${it.functionInfo.getReturnResolve()?.typeParameters?.lastOrNull()}")
                    logger.writeLogger("return type generic type name : ${it.functionInfo.getReturnElement()?.simpleName?.asString()}")
                    logger.writeLogger("parameter name : ${it.functionInfo.parameters.firstOrNull()}")
                    logger.writeLogger("parameter type : ${it.functionInfo.parameters.firstOrNull()?.type?.resolve()?.declaration?.simpleName?.asString()}\n")
                }
                .toList()

            // list merge
            val mergeMap = findDeclaredFunctions.associateBy(
                keySelector = {
                    it.key
                },
                valueTransform = {
                    it.valueType
                }
            )

            ResearchModel(
                disableSecurity = disableSecurity,
                targetClassDeclaration = classDeclaration,
                valueModels = findDeclaredFunctions,
                mergeKeyModel = mergeMap
            )
        }
        .toList()
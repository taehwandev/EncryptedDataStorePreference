package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.visitor

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.filterAnnotation
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.findClassDeclaration
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.findDisableEncrypted
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.findKeyArgument
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnElement
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnResolve
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.hasSuspend
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.DataType
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ResearchModel
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
                        it == DataStoreConst.ANNOTATION_GET_VALUE.simpleName
                                || it == DataStoreConst.ANNOTATION_SET_VALUE.simpleName
                                || it == DataStoreConst.ANNOTATION_CLEAR_VALUES.simpleName
                    }
                }
                .mapNotNull { functionDeclaration ->
                    when (functionDeclaration.annotations.first().shortName.asString()) {
                        DataStoreConst.ANNOTATION_GET_VALUE.simpleName -> {
                            // First, the return type is found in the element.
                            // If there is no information, a non generic type is searched through resolve().
                            (functionDeclaration.getReturnElement()?.simpleName
                                ?: functionDeclaration.getReturnResolve()?.simpleName)?.let { returnType ->
                                functionDeclaration.findKeyArgument()?.let { key ->
                                    DataType.Get(
                                        key = key,
                                        functionInfo = functionDeclaration,
                                        valueType = returnType,
                                        isSuspend = functionDeclaration.modifiers.hasSuspend(),
                                    )
                                }
                            }
                        }
                        DataStoreConst.ANNOTATION_SET_VALUE.simpleName -> {
                            functionDeclaration.parameters.firstOrNull()?.type?.resolve()?.declaration?.qualifiedName?.let { parameters ->
                                functionDeclaration.findKeyArgument()?.let { key ->
                                    DataType.Set(
                                        key = key,
                                        functionInfo = functionDeclaration,
                                        isSuspend = functionDeclaration.modifiers.hasSuspend(),
                                        valueType = parameters,
                                    )
                                }
                            }
                        }
                        DataStoreConst.ANNOTATION_CLEAR_VALUES.simpleName -> {
                            DataType.Clear(
                                functionInfo = functionDeclaration,
                                isSuspend = functionDeclaration.modifiers.hasSuspend(),
                            )
                        }
                        else -> null
                    }
                }
                .onEach { dataType ->
                    if (dataType is DataType.KeyValue) {
                        logger.writeLogger("------ Find new key info : ${dataType.key}")
                    }
                    logger.writeLogger("function name : ${dataType.functionInfo.simpleName.asString()}")
                    dataType.functionInfo.modifiers.forEach { modifier ->
                        logger.writeLogger("function modifier : ${modifier.name}")
                    }
                    logger.writeLogger("return type default : ${dataType.functionInfo.getReturnResolve()?.simpleName?.asString()}")
                    logger.writeLogger("return type full name : ${dataType.functionInfo.getReturnResolve()?.qualifiedName?.asString()}")
                    logger.writeLogger("return type generic : ${dataType.functionInfo.getReturnResolve()?.typeParameters?.lastOrNull()}")
                    logger.writeLogger("return type generic type name : ${dataType.functionInfo.getReturnElement()?.simpleName?.asString()}")
                    logger.writeLogger("parameter name : ${dataType.functionInfo.parameters.firstOrNull()}")
                    logger.writeLogger("parameter type : ${dataType.functionInfo.parameters.firstOrNull()?.type?.resolve()?.declaration?.simpleName?.asString()}\n")
                }
                .toList()

            // Check enable security module
            if (disableSecurity.not()) {
                val findClass = this.findClassDeclaration(DataStoreConst.USEFUL_SECURITY.packageName, DataStoreConst.USEFUL_SECURITY.simpleName)
                if (findClass == null) {
                    logger.error("Not found Useful security. add security dependency.")
                }
            }

            // list merge
            val mergeMap = findDeclaredFunctions
                .filter { dataType ->
                    dataType is DataType.KeyValue
                }
                .map { dataType ->
                    dataType as DataType.KeyValue
                }
                .associateBy(
                    keySelector = { it.key },
                    valueTransform = { it.valueType }
                )

            ResearchModel(
                disableSecurity = disableSecurity,
                targetClassDeclaration = classDeclaration,
                dataTypes = findDeclaredFunctions,
                mergeKeyModel = mergeMap
            )
        }
        .toList()
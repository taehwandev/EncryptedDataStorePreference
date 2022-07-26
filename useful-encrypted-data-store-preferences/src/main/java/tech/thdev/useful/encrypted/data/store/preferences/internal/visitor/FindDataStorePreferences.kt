package tech.thdev.useful.encrypted.data.store.preferences.internal.visitor

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import tech.thdev.useful.encrypted.data.store.preferences.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.internal.findAnnotation
import tech.thdev.useful.encrypted.data.store.preferences.internal.findKeyArgument
import tech.thdev.useful.encrypted.data.store.preferences.internal.hasSuspend
import tech.thdev.useful.encrypted.data.store.preferences.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.internal.model.ValueModel
import tech.thdev.useful.encrypted.data.store.preferences.internal.writeLogger

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

            val getValues = declaredFunctions
                .filter { functionDeclaration ->
                    functionDeclaration.findAnnotation(DataStoreConst.ANNOTATION_GET_VALUE.simpleName)
                }
                .mapNotNull { functionDeclaration ->
                    functionDeclaration
                        .findKeyArgument()?.let { key ->
                            functionDeclaration.returnType?.element?.typeArguments?.firstOrNull()?.type?.resolve()?.declaration?.qualifiedName?.let { returnType ->
                                ValueModel.Get(
                                    key = key,
                                    functionInfo = functionDeclaration,
                                    valueType = returnType,
                                )
                            }
                        }
                }
                .toList()
                .onEach {
                    logger.writeLogger("Find getValue key : ${it.key}")
//                    it.functionInfo.modifiers.forEach {
//                        logger.warn("it.functionInfo.modifiers ${it.name}")
//                    }
//                    logger.warn("return type? ${it.functionInfo.returnType?.resolve()?.declaration?.simpleName?.asString()}")
//                    logger.warn("return type? ${it.functionInfo.returnType?.resolve()?.declaration?.typeParameters?.lastOrNull()}")
//                    logger.warn("return type? ${it.functionInfo.returnType?.resolve()?.declaration?.qualifiedName?.asString()}")
//                    logger.warn("return type? ${it.functionInfo.returnType?.element?.typeArguments?.firstOrNull()}")
//                    logger.warn("return type? ${it.functionInfo.returnType?.element?.typeArguments?.firstOrNull()?.type?.resolve()?.declaration?.simpleName?.asString()}")
//                    logger.warn("return type? ${it.functionInfo.functionKind.name}")
//                    logger.warn("return type? ${it.functionInfo.simpleName.asString()}")
//                    logger.warn("return type? ${it.functionInfo.parameters?.firstOrNull()}")
//                    logger.warn("return type? ${it.functionInfo.isExpect}")
//                    logger.warn("return type? ${it.functionInfo.isAbstract}")
//                    logger.warn("return type? ${it.functionInfo.extensionReceiver}")
//                    logger.warn("return type? ${it.functionInfo.parameters}")
                }

            logger.writeLogger("-------------------")

            val setValues = declaredFunctions
                .filter { functionDeclaration ->
                    functionDeclaration.findAnnotation(DataStoreConst.ANNOTATION_SET_VALUE.simpleName)
                }
                .mapNotNull { functionDeclaration ->
                    functionDeclaration
                        .findKeyArgument()?.let { key ->
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
                .toList()
                .onEach {
//                    it.functionInfo.parameters.forEach {
//                        logger.warn("ksValueParameter ${it.name?.asString()}")
//                    }
//                    it.functionInfo.modifiers.forEach {
//                        logger.warn("it.functionInfo.modifiers ${it.name}")
//                    }
//
//                    logger.warn("it.functionInfo.returnType?.element?.typeArguments?.firstOrNull() ${it.functionInfo.returnType?.element?.typeArguments?.firstOrNull()}")
//                    logger.warn("it.functionInfo.functionKind.name ${it.functionInfo.functionKind.name}")
//                    logger.warn("it.functionInfo.simpleName.asString() ${it.functionInfo.simpleName.asString()}")
//                    logger.warn("it.functionInfo.parameters.firstOrNull() ${it.functionInfo.parameters.firstOrNull()}")
//                    logger.warn("it.functionInfo.parameters.firstOrNull()?.type?.resolve()?.declaration?.simpleName?.asString() ${it.functionInfo.parameters.firstOrNull()?.type?.resolve()?.declaration?.simpleName?.asString()}")
//                    logger.warn("it.functionInfo.functionKind ${it.functionInfo.findOverridee()?.typeParameters}")
//                    logger.warn("it.key ${it.key}")
                    logger.writeLogger("Find setValue key : ${it.key}")
                }

            // list merge
            val mergeMap = (getValues + setValues).associateBy(
                keySelector = {
                    it.key
                },
                valueTransform = {
                    it.valueType
                }
            )

            ResearchModel(
                targetClassDeclaration = classDeclaration,
                getValueModel = getValues,
                setValueModel = setValues,
                mergeKeyModel = mergeMap
            )
        }
        .toList()

// 순서
// getValue -> find key, fun return type
// setValue -> find key, fun return type

// getValue key and setValue key merge -> find type
// generate data class

// generate impl DataStore<Data class>


// getValue 찾고
// setValue 찾아서 merge 후 data class
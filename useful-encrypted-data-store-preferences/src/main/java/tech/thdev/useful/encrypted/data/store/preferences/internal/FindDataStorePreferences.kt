package tech.thdev.useful.encrypted.data.store.preferences.internal

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

internal fun Resolver.findUsefulPreferences(
    logger: KSPLogger,
): Sequence<KSClassDeclaration> {
    logger.error("DataStoreConst.ANNOTATION_USEFUL_PREFERENCES.canonicalName ${DataStoreConst.ANNOTATION_USEFUL_PREFERENCES.canonicalName}")
    return getSymbolsWithAnnotation(DataStoreConst.ANNOTATION_USEFUL_PREFERENCES.canonicalName)
        .filter { ksAnnotated ->
            logger.error(ksAnnotated.origin.name)
            ksAnnotated is KSClassDeclaration
        }
        .map {
            it as KSClassDeclaration
        }
        .map {
            logger.error(it.simpleName.asString())
            it
        }
}
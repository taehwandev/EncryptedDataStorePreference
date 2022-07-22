package tech.thdev.useful.encrypted.data.store.preferences

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.asClassName
import tech.thdev.useful.encrypted.data.store.preferences.annotations.TestA
import tech.thdev.useful.encrypted.data.store.preferences.internal.DataStoreConst

class EncryptedDataStorePreferencesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("process?? find testA ${TestA::class.asClassName().canonicalName}")
//        resolver.findUsefulPreferences(logger)
        resolver.getSymbolsWithAnnotation(TestA::class.asClassName().canonicalName)
            .filter { ksAnnotated ->
                logger.warn("findKSAnnotated ${ksAnnotated is KSClassDeclaration}}", ksAnnotated)
                ksAnnotated is KSClassDeclaration
            }
            .map { ksAnnotated ->
                ksAnnotated as KSClassDeclaration
            }
            .forEach {
                logger.warn("TestA ${it.simpleName.asString()}")
            }

        logger.warn("process?? find testA ${DataStoreConst.ANNOTATION_USEFUL_PREFERENCES.canonicalName}")
        resolver.getSymbolsWithAnnotation(DataStoreConst.ANNOTATION_USEFUL_PREFERENCES.canonicalName)
            .filter { ksAnnotated ->
                logger.warn(ksAnnotated.origin.name)
                ksAnnotated is KSClassDeclaration
            }
            .map {
                it as KSClassDeclaration
            }
            .forEach {
                logger.warn("UsefulPreferences ${it.simpleName.asString()}")
            }

        return emptyList()
    }
}
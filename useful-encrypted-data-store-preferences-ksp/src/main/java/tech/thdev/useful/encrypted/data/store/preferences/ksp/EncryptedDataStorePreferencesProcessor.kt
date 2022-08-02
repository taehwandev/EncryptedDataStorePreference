package tech.thdev.useful.encrypted.data.store.preferences.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.generatePreferences
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.visitor.findUsefulPreferences

class EncryptedDataStorePreferencesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private var targetList = mutableListOf<ResearchModel>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        targetList.addAll(resolver.findUsefulPreferences(logger))
        return emptyList()
    }

    override fun finish() {
        targetList.generatePreferences(
            codeGenerator = codeGenerator,
            logger = logger,
        )
        targetList.clear()
    }
}
package tech.thdev.useful.encrypted.data.store.preferences

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import tech.thdev.useful.encrypted.data.store.preferences.internal.generate.generatePreferences
import tech.thdev.useful.encrypted.data.store.preferences.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.internal.visitor.findUsefulPreferences

class EncryptedDataStorePreferencesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private var targetList: List<ResearchModel> = emptyList()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        targetList = resolver.findUsefulPreferences(logger)

        return emptyList()
    }

    override fun finish() {
        targetList.forEach {
            it.generatePreferences(
                codeGenerator = codeGenerator,
                logger = logger,
            )
        }
    }
}
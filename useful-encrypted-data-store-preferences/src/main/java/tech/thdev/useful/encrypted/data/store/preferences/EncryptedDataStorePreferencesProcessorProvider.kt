package tech.thdev.useful.encrypted.data.store.preferences

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class EncryptedDataStorePreferencesProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        EncryptedDataStorePreferencesProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
}
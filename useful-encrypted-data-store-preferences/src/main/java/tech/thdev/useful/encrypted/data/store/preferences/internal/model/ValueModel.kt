package tech.thdev.useful.encrypted.data.store.preferences.internal.model

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSName

sealed interface ValueModel {

    val key: String
    val functionInfo: KSFunctionDeclaration
    val valueType: KSName

    data class Set(
        override val key: String,
        override val functionInfo: KSFunctionDeclaration,
        override val valueType: KSName,
        val isSuspend: Boolean,
    ) : ValueModel

    data class Get(
        override val key: String,
        override val functionInfo: KSFunctionDeclaration,
        override val valueType: KSName,
    ) : ValueModel
}
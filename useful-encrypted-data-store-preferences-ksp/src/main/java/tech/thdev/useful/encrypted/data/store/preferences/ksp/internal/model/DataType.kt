package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSName
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.hasSuspend

sealed interface DataType {

    val functionInfo: KSFunctionDeclaration
    val isSuspend: Boolean

    interface KeyValue {
        val key: String
        val valueType: KSName
    }

    data class Set(
        override val key: String,
        override val functionInfo: KSFunctionDeclaration,
        override val valueType: KSName,
        override val isSuspend: Boolean = functionInfo.modifiers.hasSuspend(),
    ) : DataType, KeyValue

    data class Get(
        override val key: String,
        override val functionInfo: KSFunctionDeclaration,
        override val valueType: KSName,
        override val isSuspend: Boolean = functionInfo.modifiers.hasSuspend(),
    ) : DataType, KeyValue

    data class Clear(
        override val functionInfo: KSFunctionDeclaration,
        override val isSuspend: Boolean = functionInfo.modifiers.hasSuspend(),
    ) : DataType
}
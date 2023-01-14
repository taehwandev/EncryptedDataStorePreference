package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal

internal fun String.mergePrefixGenerate(): String =
    "generate$this"

internal fun String.upperKey(): String =
    replace("-", "_").uppercase()

/**
 * Default value
 */
internal fun String.generateDefaultValue(defaultValue: String): String = when (this) {
    Int::class.simpleName -> "${defaultValue.toIntOrNull() ?: "0"}"
    Double::class.simpleName -> "${defaultValue.toDoubleOrNull() ?: "0.0"}"
    String::class.simpleName -> "\"$defaultValue\""
    Boolean::class.simpleName -> "${defaultValue.toBooleanStrictOrNull() ?: "false"}"
    Float::class.simpleName -> "${defaultValue.toFloatOrNull() ?: "0.0F"}"
    Long::class.simpleName -> "${defaultValue.toLongOrNull() ?: "0L"}"
    else -> {
        throw Exception("Useful Preference generate is Not support type $this")
    }
}

/**
 * Default value
 */
internal fun String.generateDefaultValueStringType(defaultValue: String): String = when (this) {
    Int::class.simpleName -> "\"${defaultValue.toIntOrNull() ?: "0"}\""
    Double::class.simpleName -> "\"${defaultValue.toDoubleOrNull() ?: "0.0"}\""
    String::class.simpleName -> "\"$defaultValue\""
    Boolean::class.simpleName -> "\"${defaultValue.toBooleanStrictOrNull() ?: "false"}\""
    Float::class.simpleName -> "\"${defaultValue.toFloatOrNull() ?: "0.0F"}\""
    Long::class.simpleName -> "\"${defaultValue.toLongOrNull() ?: "0"}\""
    else -> {
        throw Exception("Useful Preference generate is Not support type $this")
    }
}
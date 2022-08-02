package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal

internal fun String.mergePrefixGenerate(): String =
    "generate$this"

internal fun String.upperKey(): String =
    replace("-", "_").uppercase()

/**
 * Default value
 */
internal fun String.generateDefaultValue(): String = when (this) {
    Int::class.simpleName -> "0"
    Double::class.simpleName -> "0.0"
    String::class.simpleName -> "\"\""
    Boolean::class.simpleName -> "false"
    Float::class.simpleName -> "0.0f"
    Long::class.simpleName -> "0L"
    else -> {
        throw Exception("Useful Preference generate is Not support type $this")
    }
}
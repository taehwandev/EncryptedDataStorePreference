package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal

internal fun String.mergePrefixGenerate(): String =
    "generate$this"

internal fun String.upperKey(): String =
    replace("-", "_").uppercase()
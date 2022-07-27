package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.FileSpec
import java.io.OutputStreamWriter

internal fun KSFunctionDeclaration.filterAnnotation(predicate: (name: String) -> Boolean): Boolean =
    annotations.firstOrNull { ksAnnotated ->
        predicate(ksAnnotated.shortName.asString())
    } != null

/**
 * find `key` argument
 */
internal fun KSFunctionDeclaration.findKeyArgument(): String? =
    annotations.firstOrNull()?.arguments?.firstOrNull { ksValueArgument ->
        ksValueArgument.name?.asString() == DataStoreConst.ANNOTATION_KEY_ARGUMENT
    }?.let { ksValueArgument ->
        ksValueArgument.value as String
    }

/**
 * find `key` argument
 */
internal fun KSClassDeclaration.findDisableEncrypted(): Boolean =
    annotations.firstOrNull()?.arguments?.firstOrNull { ksValueArgument ->
        ksValueArgument.name?.asString() == DataStoreConst.ANNOTATION_DISABLE_ENCRYPTED
    }?.let { ksValueArgument ->
        ksValueArgument.value as Boolean
    } ?: false

internal fun Set<Modifier>.hasSuspend(): Boolean =
    firstOrNull { it == Modifier.SUSPEND } != null

internal fun KSPLogger.writeLogger(message: String) {
    if (DataStoreConst.DEBUG) {
        warn(message)
    }
}

internal fun KSFunctionDeclaration.getReturnElement(): KSDeclaration? =
    returnType?.element?.typeArguments?.firstOrNull()?.type?.resolve()?.declaration

internal fun KSFunctionDeclaration.getReturnResolve(): KSDeclaration? =
    returnType?.resolve()?.declaration

internal fun KSFunctionDeclaration.hasFlow(): Boolean =
    getReturnResolve()?.simpleName?.asString() == DataStoreConst.FLOW.simpleName

internal fun FileSpec.writeTo(
    codeGenerator: CodeGenerator,
    packageName: String,
    fileName: String,
) {
    val outputStream = codeGenerator.createNewFile(
        Dependencies.ALL_FILES,
        packageName,
        fileName
    )

    OutputStreamWriter(outputStream, "UTF-8").use { writeTo(it) }
}

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

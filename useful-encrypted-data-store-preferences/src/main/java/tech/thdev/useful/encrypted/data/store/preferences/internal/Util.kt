package tech.thdev.useful.encrypted.data.store.preferences.internal

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.FileSpec
import java.io.OutputStreamWriter

internal fun KSFunctionDeclaration.findAnnotation(findAnnotationName: String): Boolean =
    annotations.firstOrNull { ksAnnotated ->
        ksAnnotated.shortName.asString() == findAnnotationName
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

internal fun Set<Modifier>.hasSuspend(): Boolean =
    firstOrNull { it == Modifier.SUSPEND } != null

internal fun KSPLogger.writeLogger(message: String) {
    if (DataStoreConst.DEBUG) {
        warn(message)
    }
}

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

internal fun String.upperKey(): String =
    replace("-", "_").uppercase()

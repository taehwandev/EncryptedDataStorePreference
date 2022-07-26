package tech.thdev.useful.encrypted.data.store.preferences.internal.generate

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import tech.thdev.useful.encrypted.data.store.preferences.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.internal.upperKey
import tech.thdev.useful.encrypted.data.store.preferences.internal.writeTo

internal class GeneratePreferences(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) {

    fun generate(researchModel: ResearchModel) {
        val className = researchModel.targetClassDeclaration.simpleName.getShortName()
        val packageName = researchModel.targetClassDeclaration.packageName.asString()

        val keyClassName = researchModel.mergeKeyModel.generateKeysClass(className, packageName)
        generateImplClass(className, packageName)
    }

    private fun Map<String, KSName>.generateKeysClass(
        className: String,
        packageName: String,
    ): ClassName {
        val newClassName = "${className}Keys"
        val fileSpec = FileSpec.builder(packageName = packageName, fileName = newClassName)
        fileSpec.addImport(DataStoreConst.PREF_PREFERENCES.packageName, DataStoreConst.PREF_PREFERENCES.simpleName)
        fileSpec.addImport(DataStoreConst.PREF_PREFERENCES_KEY.packageName, DataStoreConst.PREF_PREFERENCES_KEY.simpleName)

        val classSpec = TypeSpec.objectBuilder(name = newClassName)
        forEach { (key, type) ->
            val propertySpec = when (type.asString()) {
                Int::class.qualifiedName -> key.createProperty(type.asString(), DataStoreConst.PREF_GENERATE_INT, fileSpec)
                Double::class.qualifiedName -> key.createProperty(type.asString(), DataStoreConst.PREF_GENERATE_DOUBLE, fileSpec)
                String::class.qualifiedName -> key.createProperty(type.asString(), DataStoreConst.PREF_GENERATE_STRING, fileSpec)
                Boolean::class.qualifiedName -> key.createProperty(type.asString(), DataStoreConst.PREF_GENERATE_BOOLEAN, fileSpec)
                Float::class.qualifiedName -> key.createProperty(type.asString(), DataStoreConst.PREF_GENERATE_FLOAT, fileSpec)
                Long::class.qualifiedName -> key.createProperty(type.asString(), DataStoreConst.PREF_GENERATE_LONG, fileSpec)
                else -> {
                    throw Exception("Useful Preference generate is Not support type ${type.asString()}, ${String::class.simpleName}")
                }
            }
            classSpec.addProperty(propertySpec.build())
        }

        fileSpec.addType(classSpec.build())

        // Write file
        fileSpec.build().writeTo(
            codeGenerator = codeGenerator,
            packageName = packageName,
            fileName = newClassName,
        )

        return ClassName(packageName = packageName, newClassName)
    }

    private fun generateImplClass(
        className: String,
        packageName: String,
    ) {
        val newClassName = "${className}Impl"
        val fileSpec = FileSpec.builder(packageName = packageName, fileName = newClassName)

        val classSpec = TypeSpec.classBuilder(name = newClassName)

        fileSpec.addType(classSpec.build())

        fileSpec.build().writeTo(
            codeGenerator = codeGenerator,
            packageName = packageName,
            fileName = newClassName,
        )
    }

    private fun String.createProperty(type: String, typeBuilder: ClassName, fileSpec: FileSpec.Builder): PropertySpec.Builder {
        fileSpec.addImport(typeBuilder.packageName, typeBuilder.simpleName)

        return PropertySpec
            .builder(
                name = upperKey(),
                type = DataStoreConst.PREF_PREFERENCES_KEY.parameterizedBy(TypeVariableName(type)),
            )
            .initializer("${typeBuilder.simpleName}(\"$this\")")
    }
}


internal fun ResearchModel.generatePreferences(
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
) =
    GeneratePreferences(
        logger = logger,
        codeGenerator = codeGenerator,
    ).generate(this)
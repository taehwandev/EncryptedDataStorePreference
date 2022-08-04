package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.function.generateClearFunction
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.function.generateGetFunction
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.function.generateSetFunction
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.mergePrefixGenerate
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.DataType
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.upperKey
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.writeTo

internal class GeneratePreferences(
    private val codeGenerator: CodeGenerator,
    @Suppress("unused") private val logger: KSPLogger,
) {

    fun generate(researchModels: List<ResearchModel>) {
        researchModels.forEach { item ->
            val className = item.targetClassDeclaration.simpleName.getShortName()
            val packageName = item.targetClassDeclaration.packageName.asString()

            val keyClassName = item.mergeKeyModel.generateKeysClass(item.disableSecurity, className, packageName)
            generatePreferenceImplClass(className, packageName, keyClassName, item)
        }
    }

    /**
     * Generate key class
     *
     * DataStore key class
     */
    private fun Map<String, KSName>.generateKeysClass(
        disableSecurity: Boolean,
        className: String,
        packageName: String,
    ): ClassName {
        val newClassName = "${className}Keys"
        val fileSpec = FileSpec.builder(packageName = packageName, fileName = newClassName)
            .addImport(DataStoreConst.PREF_PREFERENCES.packageName, DataStoreConst.PREF_PREFERENCES.simpleName)
            .addImport(DataStoreConst.PREF_PREFERENCES_KEY.packageName, DataStoreConst.PREF_PREFERENCES_KEY.simpleName)

        val classSpec = TypeSpec.objectBuilder(name = newClassName)
            .addModifiers(KModifier.INTERNAL)
        forEach { (key, type) ->
            val propertySpec = if (disableSecurity) {
                when (type.asString()) {
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
            } else {
                key.createProperty(String::class.qualifiedName ?: "", DataStoreConst.PREF_GENERATE_STRING, fileSpec)
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

    private fun String.createProperty(type: String, typeBuilder: ClassName, fileSpec: FileSpec.Builder): PropertySpec.Builder {
        fileSpec.addImport(typeBuilder.packageName, typeBuilder.simpleName)

        return PropertySpec
            .builder(
                name = upperKey(),
                type = DataStoreConst.PREF_PREFERENCES_KEY.parameterizedBy(TypeVariableName(type)),
            )
            .initializer("${typeBuilder.simpleName}(\"$this\")")
    }

    /**
     * internal class XXXImpl(
    private val preferencesStore: DataStore<Preferences>
    ) : XXX {

    override fun getXXX(): Flow<String> =
    userPreferencesStore.data
    .map {
    it[`UserPreferencesKeys.KEY_XXX] ?: ""
    }

    override suspend fun setXxx(xxx: String) {
    userPreferencesStore.edit {
    it[`UserPreferencesKeys.KEY_XXX] = xxx
    }
    }
    }
     */
    private fun generatePreferenceImplClass(
        className: String,
        packageName: String,
        keyClassName: ClassName,
        researchModel: ResearchModel,
    ) {
        val superClass = TypeVariableName(className)
        val newClassName = "${className}Impl"
        val fileSpec = FileSpec.builder(packageName = packageName, fileName = newClassName)

        val classSpec = TypeSpec.classBuilder(name = newClassName)
            .addModifiers(KModifier.INTERNAL)
            .addSuperinterface(superClass)

        val primaryPreferencesStore = DataStoreConst.PREF_IMPL_PRIMARY_PROPERTY
        val primaryPreferencesStoreType = DataStoreConst.PREF_DATA_STORE.parameterizedBy(DataStoreConst.PREF_PREFERENCES)

        if (researchModel.disableSecurity) {
            classSpec.primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(primaryPreferencesStore, primaryPreferencesStoreType)
                    .build()
            )
                .addProperty(
                    PropertySpec.builder(primaryPreferencesStore, primaryPreferencesStoreType)
                        .initializer(primaryPreferencesStore)
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
        } else {
            val primarySecurity = DataStoreConst.USEFUL_SECURITY_PRIMARY_PROPERTY
            val primarySecurityType = DataStoreConst.USEFUL_SECURITY
            classSpec.primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(primarySecurity, primarySecurityType)
                    .addParameter(primaryPreferencesStore, primaryPreferencesStoreType)
                    .build()
            )
                .addProperty(
                    PropertySpec.builder(primarySecurity, primarySecurityType)
                        .initializer(primarySecurity)
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(primaryPreferencesStore, primaryPreferencesStoreType)
                        .initializer(primaryPreferencesStore)
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
        }

        // Generate functions.
        researchModel.dataTypes.forEach { item ->
            val funSpec = when (item) {
                is DataType.Get -> {
                    // Generate get function
                    item.generateGetFunction(logger, researchModel.disableSecurity, fileSpec, primaryPreferencesStore, keyClassName.simpleName)
                }
                is DataType.Set -> {
                    // Generate set function
                    item.generateSetFunction(researchModel.disableSecurity, fileSpec, primaryPreferencesStore, keyClassName.simpleName)
                }
                is DataType.Clear -> {
                    // Generate clear
                    item.generateClearFunction(fileSpec, primaryPreferencesStore)
                }
            }
            classSpec.addFunction(funSpec.build())
        }

        fileSpec.addType(classSpec.build())

        // generate function
        // DataStore<Preferences>.generateXXX(): XXX = XXXImpl(this)
        if (researchModel.disableSecurity) {
            val funSpec = FunSpec.builder(className.mergePrefixGenerate())
                .returns(superClass)
                .receiver(primaryPreferencesStoreType)
                .addStatement("return %N(%N = this)", newClassName, DataStoreConst.PREF_IMPL_PRIMARY_PROPERTY)
            fileSpec.addFunction(funSpec.build())
        } else {
            val funSpec = FunSpec.builder(className.mergePrefixGenerate())
                .returns(superClass)
                .receiver(primaryPreferencesStoreType)
                .addParameter(ParameterSpec.builder(DataStoreConst.USEFUL_SECURITY_PRIMARY_PROPERTY, DataStoreConst.USEFUL_SECURITY).build())
                .addStatement(
                    "return %N(%N = this, %N = %N)",
                    newClassName,
                    DataStoreConst.PREF_IMPL_PRIMARY_PROPERTY,
                    DataStoreConst.USEFUL_SECURITY_PRIMARY_PROPERTY,
                    DataStoreConst.USEFUL_SECURITY_PRIMARY_PROPERTY,
                )
            fileSpec.addFunction(funSpec.build())
        }

        fileSpec.build().writeTo(
            codeGenerator = codeGenerator,
            packageName = packageName,
            fileName = newClassName,
        )
    }
}

internal fun List<ResearchModel>.generatePreferences(
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
) =
    GeneratePreferences(
        logger = logger,
        codeGenerator = codeGenerator,
    ).generate(this)
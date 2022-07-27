package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generateDefaultValue
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnElement
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnResolve
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.hasFlow
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.mergePrefixGenerate
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ResearchModel
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.ValueModel
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.upperKey
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.writeTo

internal class GeneratePreferences(
    private val codeGenerator: CodeGenerator,
    @Suppress("unused") private val logger: KSPLogger,
) {

    fun generate(researchModel: ResearchModel) {
        val className = researchModel.targetClassDeclaration.simpleName.getShortName()
        val packageName = researchModel.targetClassDeclaration.packageName.asString()

        val keyClassName = researchModel.mergeKeyModel.generateKeysClass(className, packageName)
        generatePreferenceImplClass(className, packageName, keyClassName, researchModel)
    }

    /**
     * Generate key class
     *
     * DataStore key class
     */
    private fun Map<String, KSName>.generateKeysClass(
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

        val primaryContractValue = DataStoreConst.PREF_IMPL_PRIMARY_PROPERTY
        val primaryConstructType = DataStoreConst.PREF_DATA_STORE.parameterizedBy(DataStoreConst.PREF_PREFERENCES)
        val classSpec = TypeSpec.classBuilder(name = newClassName)
            .addModifiers(KModifier.INTERNAL)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(primaryContractValue, primaryConstructType)
                    .build()
            )
            .addProperty(
                PropertySpec.builder(primaryContractValue, primaryConstructType)
                    .initializer(primaryContractValue)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addSuperinterface(superClass)

        researchModel.valueModels.forEach { item ->
            when (item) {
                is ValueModel.Get -> {
                    // Generate get function
                    val funSpec = item.generateGetFunction(fileSpec, primaryContractValue, keyClassName.simpleName)
                    classSpec.addFunction(funSpec.build())
                }
                is ValueModel.Set -> {
                    // Generate set function
                    val funSpec = item.generateSetFunction(fileSpec, primaryContractValue, keyClassName.simpleName)
                    classSpec.addFunction(funSpec.build())
                }
            }
        }

        fileSpec.addType(classSpec.build())

        // generate function
        // DataStore<Preferences>.generateXXX(): XXX = XXXImpl(this)
        val funSpec = FunSpec.builder(className.mergePrefixGenerate())
            .returns(superClass)
            .receiver(primaryConstructType)
            .addStatement("return %N(this)", newClassName)

        fileSpec.addFunction(funSpec.build())

        fileSpec.build().writeTo(
            codeGenerator = codeGenerator,
            packageName = packageName,
            fileName = newClassName,
        )
    }

    private fun ValueModel.Set.generateSetFunction(
        fileSpec: FileSpec.Builder,
        primaryContractValue: String,
        keyClassName: String,
    ): FunSpec.Builder {
        val funSpec = FunSpec.builder(functionInfo.simpleName.asString())
            .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
        if (isSuspend) {
            funSpec.addModifiers(KModifier.SUSPEND)
        }

        val parameter = functionInfo.parameters.firstOrNull()
        if (parameter != null && parameter.name?.asString() != null) {
            val parameterName = parameter.name!!.asString()
            funSpec.addParameter(parameterName, TypeVariableName(parameter.type.resolve().declaration.simpleName.asString()))

            // generate body
            fileSpec.addImport(DataStoreConst.PREF_DATA_STORE_EDIT.packageName, DataStoreConst.PREF_DATA_STORE_EDIT.simpleName)
            funSpec.addStatement(
                "$primaryContractValue.${DataStoreConst.PREF_DATA_STORE_EDIT.simpleName}" +
                        " {\nit[$keyClassName.${key.upperKey()}] = $parameterName\n}"
            )
        }
        return funSpec
    }

    /**
     * override fun getXXX(): Flow<XXX> =
    userPreferencesStore.data
    .map {
    it[`XXXKeys.KEY_XXX] ?: `defaultValue`
    }
     */
    private fun ValueModel.Get.generateGetFunction(
        fileSpec: FileSpec.Builder,
        primaryContractValue: String,
        keyClassName: String,
    ): FunSpec.Builder {
        val funSpec = FunSpec.builder(functionInfo.simpleName.asString())
            .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)

        if (isSuspend) { // flow to value
            fileSpec.addImport(DataStoreConst.FLOW_FIRST.packageName, DataStoreConst.FLOW_FIRST.simpleName)

            funSpec.addModifiers(KModifier.SUSPEND)
                .returns(TypeVariableName(valueType.getShortName()))
                .addCode(
                    "return $primaryContractValue.data" +
                            "\n.map {" +
                            "\nit[$keyClassName.${key.upperKey()}] ?: ${valueType.getShortName().generateDefaultValue()}" +
                            "\n}" +
                            "\n.first()"
                )
        } else if (functionInfo.hasFlow()) { // flow return
            // find generic type
            functionInfo.getReturnResolve()?.typeParameters?.firstOrNull()?.let { _ ->
                // get full name
                if (functionInfo.getReturnResolve()?.qualifiedName != null && functionInfo.getReturnElement()?.simpleName != null) {
                    val genericName = functionInfo.getReturnResolve()!!.qualifiedName!!
                    val typeName = functionInfo.getReturnElement()!!.simpleName.getShortName()
                    val genericClassName = ClassName(packageName = genericName.getQualifier(), genericName.getShortName())
                    funSpec.returns(genericClassName.parameterizedBy(TypeVariableName(typeName)))

                    fileSpec.addImport(DataStoreConst.FLOW_MAP.packageName, DataStoreConst.FLOW_MAP.simpleName)
                    funSpec.addCode(
                        "return $primaryContractValue.data" +
                                "\n.map {" +
                                "\nit[$keyClassName.${key.upperKey()}] ?: ${typeName.generateDefaultValue()}" +
                                "\n}"
                    )
                }
            }
        } else {
            throw Exception("Not support type")
        }

        return funSpec
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
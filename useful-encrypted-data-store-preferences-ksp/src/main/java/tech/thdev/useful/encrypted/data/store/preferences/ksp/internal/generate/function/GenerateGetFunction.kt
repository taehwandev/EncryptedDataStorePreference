package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.function

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generateDefaultValue
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnElement
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.getReturnResolve
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.hasFlow
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.DataType
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.upperKey

/**
 * Generate getFunction
 *
override fun getXXX(): Flow<XXX> =
userPreferencesStore.data
.map {
it[`XXXKeys.KEY_XXX] ?: `defaultValue`
}
 */
internal fun DataType.Get.generateGetFunction(
    disableSecurity: Boolean,
    fileSpec: FileSpec.Builder,
    primaryContractValue: String,
    keyClassName: String,
): FunSpec.Builder {
    val funSpec = FunSpec.builder(functionInfo.simpleName.asString())
        .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)

    fun String.getMap() =
        "return $primaryContractValue.data" +
                "\n.map {" +
                "\nit[$keyClassName.${key.upperKey()}] ?: ${this.generateDefaultValue()}" +
                "\n}"

    fun String.getMapDecrypt() =
        "return $primaryContractValue.data" +
                "\n.mapDecrypt<$this>(${DataStoreConst.USEFUL_SECURITY_PRIMARY_PROPERTY}, $this::class) {" +
                "\nit[$keyClassName.${key.upperKey()}]" +
                "\n}"

    if (isSuspend) { // flow to value
        fileSpec.addImport(DataStoreConst.FLOW_FIRST.packageName, DataStoreConst.FLOW_FIRST.simpleName)

        funSpec.addModifiers(KModifier.SUSPEND)
            .returns(TypeVariableName(valueType.getShortName()))

        if (disableSecurity) {
            fileSpec.addImport(DataStoreConst.FLOW_MAP.packageName, DataStoreConst.FLOW_MAP.simpleName)
            funSpec.addCode("${valueType.getShortName().getMap()}\n.first()")
        } else {
            fileSpec.addImport(DataStoreConst.FLOW_MAP_DECRYPT.packageName, DataStoreConst.FLOW_MAP_DECRYPT.simpleName)
            funSpec.addCode("${valueType.getShortName().getMapDecrypt()}\n.first()")
        }
    } else if (functionInfo.hasFlow()) { // flow return
        // find generic type
        functionInfo.getReturnResolve()?.typeParameters?.firstOrNull()?.let { _ ->
            // get full name
            if (functionInfo.getReturnResolve()?.qualifiedName != null && functionInfo.getReturnElement()?.simpleName != null) {
                val genericName = functionInfo.getReturnResolve()!!.qualifiedName!!
                val typeName = functionInfo.getReturnElement()!!.simpleName.getShortName()
                val genericClassName = ClassName(packageName = genericName.getQualifier(), genericName.getShortName())
                funSpec.returns(genericClassName.parameterizedBy(TypeVariableName(typeName)))

                if (disableSecurity) {
                    fileSpec.addImport(DataStoreConst.FLOW_MAP.packageName, DataStoreConst.FLOW_MAP.simpleName)
                    funSpec.addCode(typeName.getMap())
                } else {
                    fileSpec.addImport(DataStoreConst.FLOW_MAP_DECRYPT.packageName, DataStoreConst.FLOW_MAP_DECRYPT.simpleName)
                    funSpec.addCode(typeName.getMapDecrypt())
                }
            }
        }
    } else {
        throw Exception("${functionInfo.simpleName.getShortName()} Not support type")
    }

    return funSpec
}
package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.function

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeVariableName
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.DataType
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.upperKey

/**
 * Generate set function.
 *
public override suspend fun setXXX(`value`: Type): Unit {
preferencesStore.edit {
it[`SecurityPreferencesKeys.KEY_DOUBLE] = value
}
}
 */
internal fun DataType.Set.generateSetFunction(
    disableSecurity: Boolean,
    fileSpec: FileSpec.Builder,
    primaryContractValue: String,
    keyClassName: String,
): FunSpec.Builder {
    val funSpec = FunSpec.builder(functionInfo.simpleName.asString())
        .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
    if (isSuspend) {
        funSpec.addModifiers(KModifier.SUSPEND)
    }

    functionInfo.parameters.firstOrNull().takeIf { it?.name?.asString() != null }?.let { parameter ->
        val parameterName = parameter.name!!.asString()
        funSpec.addParameter(parameterName, TypeVariableName(parameter.type.resolve().declaration.simpleName.asString()))

        // generate body
        if (disableSecurity) {
            fileSpec.addImport(DataStoreConst.PREF_DATA_STORE_EDIT.packageName, DataStoreConst.PREF_DATA_STORE_EDIT.simpleName)
            funSpec.addStatement(
                "$primaryContractValue.${DataStoreConst.PREF_DATA_STORE_EDIT.simpleName}" +
                        " {\nit[$keyClassName.${key.upperKey()}] = $parameterName\n}"
            )
        } else {
            fileSpec.addImport(DataStoreConst.EDIT_ENCRYPT.packageName, DataStoreConst.EDIT_ENCRYPT.simpleName)
            funSpec.addStatement(
                "$primaryContractValue.${DataStoreConst.EDIT_ENCRYPT.simpleName}" +
                        "(${DataStoreConst.USEFUL_SECURITY_PRIMARY_PROPERTY}, $parameterName) { preferences, encrypted ->" +
                        " \npreferences[$keyClassName.${key.upperKey()}] = encrypted\n}"
            )
        }
    }
    return funSpec
}
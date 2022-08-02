package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.generate.function

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.DataStoreConst
import tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model.DataType

internal fun DataType.Clear.generateClearFunction(
    fileSpec: FileSpec.Builder,
    primaryContractValue: String,
): FunSpec.Builder {
    val funSpec = FunSpec.builder(functionInfo.simpleName.asString())
        .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
    if (isSuspend) {
        funSpec.addModifiers(KModifier.SUSPEND)
    }

    // generate body
    fileSpec.addImport(DataStoreConst.PREF_DATA_STORE_EDIT.packageName, DataStoreConst.PREF_DATA_STORE_EDIT.simpleName)
    funSpec.addStatement(
        "$primaryContractValue.${DataStoreConst.PREF_DATA_STORE_EDIT.simpleName}" +
                " {\nit.clear()\n}"
    )
    return funSpec
}
package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal.model

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSName

data class ResearchModel(
    val disableSecurity: Boolean,
    val targetClassDeclaration: KSClassDeclaration,
    val valueModels: List<ValueModel>,
    val mergeKeyModel: Map<String, KSName>,
)
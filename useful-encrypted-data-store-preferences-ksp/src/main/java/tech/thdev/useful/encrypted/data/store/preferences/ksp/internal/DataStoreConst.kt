package tech.thdev.useful.encrypted.data.store.preferences.ksp.internal

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import kotlinx.coroutines.flow.Flow
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue

internal object DataStoreConst {

    const val DEBUG = true

    val ANNOTATION_USEFUL_PREFERENCES = UsefulPreferences::class.asClassName()
    const val ANNOTATION_DISABLE_SECURITY = "disableSecurity"

    val ANNOTATION_GET_VALUE = GetValue::class.asClassName()
    val ANNOTATION_SET_VALUE = SetValue::class.asClassName()
    const val ANNOTATION_KEY_ARGUMENT = "key"

    val ANNOTATION_CLEAR_VALUES = ClearValues::class.asClassName()

    val PREDEFINED_ANNOTATIONS = arrayOf(
        ANNOTATION_GET_VALUE.simpleName, ANNOTATION_SET_VALUE.simpleName, ANNOTATION_CLEAR_VALUES.simpleName,
    )

    // data store preference
    val PREF_GENERATE_INT = ClassName("androidx.datastore.preferences.core", "intPreferencesKey")
    val PREF_GENERATE_DOUBLE = ClassName("androidx.datastore.preferences.core", "doublePreferencesKey")
    val PREF_GENERATE_STRING = ClassName("androidx.datastore.preferences.core", "stringPreferencesKey")
    val PREF_GENERATE_BOOLEAN = ClassName("androidx.datastore.preferences.core", "booleanPreferencesKey")
    val PREF_GENERATE_FLOAT = ClassName("androidx.datastore.preferences.core", "floatPreferencesKey")
    val PREF_GENERATE_LONG = ClassName("androidx.datastore.preferences.core", "longPreferencesKey")
    val PREF_PREFERENCES = ClassName("androidx.datastore.preferences.core", "Preferences")
    val PREF_PREFERENCES_KEY = ClassName("androidx.datastore.preferences.core", "Preferences.Key")
    val PREF_DATA_STORE = ClassName("androidx.datastore.core", "DataStore")
    val PREF_DATA_STORE_EDIT = ClassName("androidx.datastore.preferences.core", "edit")
    const val PREF_IMPL_PRIMARY_PROPERTY = "preferencesStore"

    // Coroutine
    val FLOW = Flow::class.asClassName()
    val FLOW_MAP = ClassName("kotlinx.coroutines.flow", "map")
    val FLOW_FIRST = ClassName("kotlinx.coroutines.flow", "first")

    // security
    const val USEFUL_SECURITY_PRIMARY_PROPERTY = "usefulSecurity"
    val USEFUL_SECURITY = ClassName("tech.thdev.useful.encrypted.data.store.preferences.security", "UsefulSecurity")
    val EDIT_ENCRYPT = ClassName("tech.thdev.useful.encrypted.data.store.preferences.extensions", "editEncrypt")
    val FLOW_MAP_DECRYPT = ClassName("tech.thdev.useful.encrypted.data.store.preferences.extensions", "mapDecrypt")
}
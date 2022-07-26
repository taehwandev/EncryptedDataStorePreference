package tech.thdev.useful.encrypted.data.store.preferences.internal

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import tech.thdev.useful.encrypted.data.store.preferences.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.annotations.getter.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.annotations.setter.SetValue

internal object DataStoreConst {

    const val DEBUG = true

    val ANNOTATION_USEFUL_PREFERENCES = UsefulPreferences::class.asClassName()

    val ANNOTATION_GET_VALUE = GetValue::class.asClassName()
    val ANNOTATION_SET_VALUE = SetValue::class.asClassName()
    const val ANNOTATION_KEY_ARGUMENT = "key"

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
}
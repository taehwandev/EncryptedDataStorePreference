package tech.thdev.encrypteddatastorepreference.preference

import androidx.datastore.preferences.core.stringPreferencesKey

internal object SampleKeys {

    val KEY_INT = stringPreferencesKey("key-int")

    val KEY_DOUBLE = stringPreferencesKey("key-double")

    val KEY_STRING = stringPreferencesKey("key-string")

    val KEY_BOOLEAN = stringPreferencesKey("key-boolean")

    val KEY_FLOAT = stringPreferencesKey("key-float")

    val KEY_LONG = stringPreferencesKey("key-long")
}
package tech.thdev.encrypteddatastorepreference.preference

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object SampleKeys {
  public val KEY_INT: Preferences.Key<kotlin.String> = stringPreferencesKey("key-int")

  public val KEY_DOUBLE: Preferences.Key<kotlin.Double> = doublePreferencesKey("key-double")

  public val KEY_STRING: Preferences.Key<kotlin.String> = stringPreferencesKey("key-string")

  public val KEY_BOOLEAN: Preferences.Key<kotlin.Boolean> = booleanPreferencesKey("key-boolean")

  public val KEY_FLOAT: Preferences.Key<kotlin.Float> = floatPreferencesKey("key-float")

  public val KEY_LONG: Preferences.Key<kotlin.Long> = longPreferencesKey("key-long")
}

package tech.thdev.useful.encrypted.data.store.preferences.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import tech.thdev.useful.encrypted.data.store.preferences.security.UsefulSecurity

suspend inline fun DataStore<Preferences>.editEncrypt(
    usefulSecurity: UsefulSecurity,
    value: Any,
    crossinline body: (preferences: MutablePreferences, encrypted: String) -> Unit
) {
    edit { preferences ->
        body(preferences, usefulSecurity.encryptData(value.toString()))
    }
}

inline fun <T : Any> Flow<Preferences>.mapDecrypt(
    usefulSecurity: UsefulSecurity,
    type: KClass<*>,
    defaultValue: String,
    crossinline body: (preferences: Preferences) -> String?
): Flow<T> =
    map { preferences ->
        usefulSecurity.decryptData(body(preferences) ?: "")
    }
        .distinctUntilChanged()
        .map {
            @Suppress("UNCHECKED_CAST")
            when (type) {
                Int::class -> (it.takeIf { it.isNotEmpty() } ?: defaultValue).toInt()
                Double::class -> (it.takeIf { it.isNotEmpty() } ?: defaultValue).toDouble()
                String::class -> it.takeIf { it.isNotEmpty() } ?: defaultValue
                Boolean::class -> (it.takeIf { it.isNotEmpty() } ?: defaultValue).toBoolean()
                Float::class -> (it.takeIf { it.isNotEmpty() } ?: defaultValue).toFloat()
                Long::class -> (it.takeIf { it.isNotEmpty() } ?: defaultValue).toLong()
                else -> throw IllegalArgumentException("${type.simpleName} is not support type.")
            } as T
        }
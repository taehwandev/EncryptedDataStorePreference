package tech.thdev.useful.encrypted.data.store.preferences.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import tech.thdev.useful.encrypted.data.store.preferences.security.UsefulSecurity
import tech.thdev.useful.encrypted.data.store.preferences.security.UsefulType

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
    usefulType: UsefulType,
    crossinline body: (preferences: Preferences) -> String?
): Flow<T> =
    map { preferences ->
        usefulSecurity.decryptData(body(preferences) ?: "")
    }
        .distinctUntilChanged()
        .filter { it.isNotEmpty() }
        .map {
            @Suppress("UNCHECKED_CAST")
            when (usefulType) {
                UsefulType.INT -> it.toInt()
                UsefulType.DOUBLE -> it.toDouble()
                UsefulType.STRING -> it
                UsefulType.BOOLEAN -> it.toBoolean()
                UsefulType.FLOAT -> it.toFloat()
                UsefulType.LONG -> it.toLong()
            } as T
        }
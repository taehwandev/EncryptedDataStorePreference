package tech.thdev.useful.encrypted.data.store.preferences.keystore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend inline fun <reified T> DataStore<Preferences>.editEncrypted(
    encryptedHelper: EncryptedHelper,
    value: T,
    crossinline body: (preferences: MutablePreferences, value: String) -> Unit
) {
    edit {
        val encryptedValue = encryptedHelper.encryptData(value.toString())
        body(it, encryptedValue.joinToString(","))
    }
}

inline fun Flow<Preferences>.mapDecrypted(
    encryptedHelper: EncryptedHelper,
    crossinline body: (preferences: Preferences) -> String
): Flow<String?> =
    map { preferences ->
        val data = body(preferences).split(",").map { it.toByte() }.toByteArray()
        android.util.Log.d("TEMP", "load data? $data")
        encryptedHelper.decryptData(data)
    }
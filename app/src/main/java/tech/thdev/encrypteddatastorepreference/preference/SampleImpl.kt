package tech.thdev.encrypteddatastorepreference.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import tech.thdev.useful.encrypted.data.store.preferences.keystore.EncryptedHelper
import tech.thdev.useful.encrypted.data.store.preferences.keystore.editEncrypt
import tech.thdev.useful.encrypted.data.store.preferences.keystore.mapDecrypt

class SampleImpl(
    private val preferencesStore: DataStore<Preferences>,
    private val encryptedHelper: EncryptedHelper = EncryptedHelper("my-app"),
) : Sample {
    public override fun getInt(): Flow<Int> = preferencesStore.data
        .mapDecrypt(encryptedHelper) {
            android.util.Log.d("TEMP", "Load int ${it[SampleKeys.KEY_INT] ?: ""}")
            it[SampleKeys.KEY_INT] ?: ""
        }
        .filter { it.isNullOrEmpty().not() }
        .map {
            it as Int
        }

    public override suspend fun setInt(`value`: Int): Unit {
        preferencesStore.editEncrypt(encryptedHelper, value) { preferences, encrypted ->
            preferences[SampleKeys.KEY_INT] = encrypted
        }
    }

//    public override fun getDouble(): Flow<Double> = preferencesStore.data
//        .map {
//            it[SampleKeys.KEY_DOUBLE] ?: 0.0
//        }

    public override suspend fun setDouble(`value`: Double): Unit {
        preferencesStore.editEncrypt(encryptedHelper, value) { preferences, encrypted ->
            preferences[SampleKeys.KEY_DOUBLE] = encrypted
        }
    }

    public override fun getString(): Flow<String> = preferencesStore.data
        .mapDecrypt(encryptedHelper) {
            android.util.Log.d("TEMP", "Load String ${it[SampleKeys.KEY_STRING] ?: ""}")
            it[SampleKeys.KEY_STRING] ?: ""
        }
        .filterNotNull()

    public override suspend fun setString(`value`: String): Unit {
        preferencesStore.editEncrypt(encryptedHelper, value) { preferences, value ->
            preferences[SampleKeys.KEY_STRING] = value
        }
    }

//    public override fun getBoolean(): Flow<Boolean> = preferencesStore.data
//        .map {
//            it[SampleKeys.KEY_BOOLEAN] ?: false
//        }

    public override suspend fun setBoolean(`value`: Boolean): Unit {
        preferencesStore.editEncrypt(encryptedHelper, value) { preferences, encrypted ->
            preferences[SampleKeys.KEY_BOOLEAN] = encrypted
        }
    }

//    public override fun getFloat(): Flow<Float> = preferencesStore.data
//        .map {
//            it[SampleKeys.KEY_FLOAT] ?: 0.0f
//        }

    public override suspend fun setFloat(`value`: Float): Unit {
        preferencesStore.editEncrypt(encryptedHelper, value) { preferences, encrypted ->
            preferences[SampleKeys.KEY_FLOAT] = encrypted
        }
    }

//    public override fun getLong(): Flow<Long> = preferencesStore.data
//        .map {
//            it[SampleKeys.KEY_LONG] ?: 0L
//        }

    public override suspend fun setLong(`value`: Long): Unit {
        preferencesStore.editEncrypt(encryptedHelper, value) { preferences, encrypted ->
            preferences[SampleKeys.KEY_LONG] = encrypted
        }
    }
}
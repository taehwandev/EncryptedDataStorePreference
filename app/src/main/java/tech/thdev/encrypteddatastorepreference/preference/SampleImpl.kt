package tech.thdev.encrypteddatastorepreference.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.thdev.useful.encrypted.data.store.preferences.keystore.EncryptedHelper
import tech.thdev.useful.encrypted.data.store.preferences.keystore.editEncrypted
import tech.thdev.useful.encrypted.data.store.preferences.keystore.mapDecrypted

class SampleImpl(
    private val preferencesStore: DataStore<Preferences>,
    private val encryptedHelper: EncryptedHelper = EncryptedHelper("my-app"),
) : Sample {
    public override fun getInt(): Flow<Int> = preferencesStore.data
        .mapDecrypted(encryptedHelper) {
            android.util.Log.d("TEMP", "default? ${it[SampleKeys.KEY_INT] ?: ""}")
            it[SampleKeys.KEY_INT] ?: ""
        }
        .map {
            android.util.Log.d("TEMP", "default? ${it ?: "0"}")
            it as? Int ?: 0
        }

    public override suspend fun setInt(`value`: Int): Unit {
        preferencesStore.editEncrypted(encryptedHelper, value) { preferences, value ->
            preferences[SampleKeys.KEY_INT] = value
        }
    }

    public override fun getDouble(): Flow<Double> = preferencesStore.data
        .map {
            it[SampleKeys.KEY_DOUBLE] ?: 0.0
        }

    public override suspend fun setDouble(`value`: Double): Unit {
        preferencesStore.edit {
            it[SampleKeys.KEY_DOUBLE] = value
        }
    }

    public override fun getString(): Flow<String> = preferencesStore.data
        .map {
            it[SampleKeys.KEY_STRING] ?: ""
        }

    public override suspend fun setString(`value`: String): Unit {
        preferencesStore.edit {
            it[SampleKeys.KEY_STRING] = value
        }
    }

    public override fun getBoolean(): Flow<Boolean> = preferencesStore.data
        .map {
            it[SampleKeys.KEY_BOOLEAN] ?: false
        }

    public override suspend fun setBoolean(`value`: Boolean): Unit {
        preferencesStore.edit {
            it[SampleKeys.KEY_BOOLEAN] = value
        }
    }

    public override fun getFloat(): Flow<Float> = preferencesStore.data
        .map {
            it[SampleKeys.KEY_FLOAT] ?: 0.0f
        }

    public override suspend fun setFloat(`value`: Float): Unit {
        preferencesStore.edit {
            it[SampleKeys.KEY_FLOAT] = value
        }
    }

    public override fun getLong(): Flow<Long> = preferencesStore.data
        .map {
            it[SampleKeys.KEY_LONG] ?: 0L
        }

    public override suspend fun setLong(`value`: Long): Unit {
        preferencesStore.edit {
            it[SampleKeys.KEY_LONG] = value
        }
    }
}
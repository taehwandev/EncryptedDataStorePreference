package tech.thdev.encrypteddatastorepreference.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.thdev.useful.encrypted.data.store.preferences.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.annotations.getter.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.annotations.setter.SetValue

@UsefulPreferences
interface UserPreferences {

    @GetValue(KEY_USER_INT)
    fun getInt(): Flow<Int>

    @SetValue(KEY_USER_INT)
    suspend fun setInt(value: Int)

    @GetValue(KEY_USER_DOUBLE)
    fun getDouble(): Flow<Double>

    @SetValue(KEY_USER_DOUBLE)
    suspend fun setDouble(value: Double)

    @GetValue(KEY_USER_STRING)
    fun getString(): Flow<String>

    @SetValue(KEY_USER_STRING)
    suspend fun setString(value: String)

    @GetValue(KEY_USER_BOOLEAN)
    fun getBoolean(): Flow<Boolean>

    @SetValue(KEY_USER_BOOLEAN)
    suspend fun setBoolean(value: Boolean)

    @GetValue(KEY_USER_FLOAT)
    fun getFloat(): Flow<Float>

    @SetValue(KEY_USER_FLOAT)
    suspend fun setFloat(value: Float)

    @GetValue(KEY_USER_LONG)
    fun getLong(): Flow<Long>

    @SetValue(KEY_USER_LONG)
    suspend fun setLong(value: Long)

    companion object {

        private const val KEY_USER_INT = "key-user-int"
        private const val KEY_USER_DOUBLE = "key-user-double"
        private const val KEY_USER_STRING = "key-user-string"
        private const val KEY_USER_BOOLEAN = "key-user-boolean"
        private const val KEY_USER_FLOAT = "key-user-float"
        private const val KEY_USER_LONG = "key-user-long"
    }
}

//class UserPreferencesImpl(
//    private val userPreferencesStore: DataStore<Preferences>
//) : UserPreferences {
//
//    override fun getUserId(): Flow<String> =
//        userPreferencesStore.data
//            .map {
//                it[UserPreferencesKeys.KEY_USER_ID] ?: ""
//            }
//
//    override suspend fun setUserId(userId: String) {
//        userPreferencesStore.edit {
//            it[UserPreferencesKeys.KEY_USER_ID] = userId
//        }
//    }
//}
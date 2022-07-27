package tech.thdev.samplepreference.repository

import kotlinx.coroutines.flow.Flow
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.getter.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.setter.SetValue

@UsefulPreferences
interface SamplePreferences {

    @GetValue(KEY_INT)
    fun getInt(): Flow<Int>

    @GetValue(KEY_INT)
    suspend fun getIntValue(): Int

    @SetValue(KEY_INT)
    suspend fun setInt(value: Int)

    @GetValue(KEY_DOUBLE)
    fun getDouble(): Flow<Double>

    @SetValue(KEY_DOUBLE)
    suspend fun setDouble(value: Double)

    @GetValue(KEY_STRING)
    fun getString(): Flow<String>

    @SetValue(KEY_STRING)
    suspend fun setString(value: String)

    @GetValue(KEY_BOOLEAN)
    fun getBoolean(): Flow<Boolean>

    @SetValue(KEY_BOOLEAN)
    suspend fun setBoolean(value: Boolean)

    @GetValue(KEY_FLOAT)
    fun getFloat(): Flow<Float>

    @SetValue(KEY_FLOAT)
    suspend fun setFloat(value: Float)

    @GetValue(KEY_LONG)
    fun getLong(): Flow<Long>

    @SetValue(KEY_LONG)
    suspend fun setLong(value: Long)

    companion object {

        private const val KEY_INT = "key-int"
        private const val KEY_DOUBLE = "key-double"
        private const val KEY_STRING = "key-string"
        private const val KEY_BOOLEAN = "key-boolean"
        private const val KEY_FLOAT = "key-float"
        private const val KEY_LONG = "key-long"
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
//}
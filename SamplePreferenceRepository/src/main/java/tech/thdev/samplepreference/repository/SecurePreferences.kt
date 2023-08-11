package tech.thdev.samplepreference.repository

import kotlinx.coroutines.flow.Flow
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue

@UsefulPreferences
interface SecurePreferences {

    @GetValue(KEY_INT, "200")
    fun flowInt(): Flow<Int>

    @GetValue(KEY_INT, "200")
    suspend fun getInt(): Int

    @SetValue(KEY_INT)
    suspend fun setInt(value: Int)

    @GetValue(KEY_DOUBLE, "0.0153")
    fun flowDouble(): Flow<Double>

    @SetValue(KEY_DOUBLE)
    suspend fun setDouble(value: Double)

    @GetValue(KEY_STRING, "default string")
    fun flowString(): Flow<String>

    @SetValue(KEY_STRING)
    suspend fun setString(value: String)

    @GetValue(KEY_BOOLEAN, "true")
    fun flowBoolean(): Flow<Boolean>

    @SetValue(KEY_BOOLEAN)
    suspend fun setBoolean(value: Boolean)

    @GetValue(KEY_FLOAT, "1234.11")
    fun flowFloat(): Flow<Float>

    @SetValue(KEY_FLOAT)
    suspend fun setFloat(value: Float)

    @GetValue(KEY_LONG, "949")
    fun flowLong(): Flow<Long>

    @SetValue(KEY_LONG)
    suspend fun setLong(value: Long)

    @ClearValues
    suspend fun clearAll()

    companion object {

        private const val KEY_INT = "key-int"
        private const val KEY_DOUBLE = "key-double"
        private const val KEY_STRING = "key-string"
        private const val KEY_BOOLEAN = "key-boolean"
        private const val KEY_FLOAT = "key-float"
        private const val KEY_LONG = "key-long"
    }
}
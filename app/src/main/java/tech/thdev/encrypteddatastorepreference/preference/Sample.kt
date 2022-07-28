package tech.thdev.encrypteddatastorepreference.preference

import kotlinx.coroutines.flow.Flow

interface Sample {

    fun getInt(): Flow<Int>

    suspend fun setInt(value: Int)

//    fun getDouble(): Flow<Double>

    suspend fun setDouble(value: Double)

    fun getString(): Flow<String>

    suspend fun setString(value: String)

//    fun getBoolean(): Flow<Boolean>

    suspend fun setBoolean(value: Boolean)

//    fun getFloat(): Flow<Float>

    suspend fun setFloat(value: Float)

//    fun getLong(): Flow<Long>

    suspend fun setLong(value: Long)
}
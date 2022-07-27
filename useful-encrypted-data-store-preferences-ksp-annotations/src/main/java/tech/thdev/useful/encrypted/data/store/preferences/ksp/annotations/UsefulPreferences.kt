package tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations

/**
 * Use DataStorePreference
 *
 * Use UsefulPreference.
 *
 * Sample code
<pre>
@UsefulPreferences(/* options disableEncrypted = true */)
interface XXXPreferences {

@GetValue(KEY_INT)
fun getInt(): Flow<Int>
// oro
@GetValue(KEY_INT)


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
</pre>
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class UsefulPreferences(@Suppress("unused") val disableEncrypted: Boolean = false)
package tech.thdev.useful.encrypted.data.store.preferences.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import java.io.File
import java.nio.file.Path
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class EncryptedDataStorePreferencesProcessorProviderTest {

    @Test
    fun `test NonSecurePreferences`() {
        val securityClass = SourceFile.kotlin(
            "UsefulSecurity.kt",
            """
            package tech.thdev.useful.encrypted.data.store.preferences.security

            interface UsefulSecurity {

                /**
                 * Data to encryptData
                 */
                fun encryptData(data: String): String

                /**
                 * encryptData to decrypt
                 */
                fun decryptData(encryptData: String): String
            }
        """.trimIndent()
        )

        val securityPreferences = SourceFile.kotlin(
            "SecurityPreferences.kt",
            """
                package tech.thdev.samplepreference.repository
                
                import kotlinx.coroutines.flow.Flow
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue
                
                @UsefulPreferences(disableSecure = true)
                interface SecurityPreferences {
                
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
            """.trimIndent()
        )

        val compilationResult = compile(securityClass, securityPreferences)

        // SecurityPreferencesKeys
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.Preferences.Key
                import androidx.datastore.preferences.core.booleanPreferencesKey
                import androidx.datastore.preferences.core.doublePreferencesKey
                import androidx.datastore.preferences.core.floatPreferencesKey
                import androidx.datastore.preferences.core.intPreferencesKey
                import androidx.datastore.preferences.core.longPreferencesKey
                import androidx.datastore.preferences.core.stringPreferencesKey
                
                internal object SecurityPreferencesKeys {
                  public val KEY_INT: Preferences.Key<kotlin.Int> = intPreferencesKey("key-int")
                
                  public val KEY_DOUBLE: Preferences.Key<kotlin.Double> = doublePreferencesKey("key-double")
                
                  public val KEY_STRING: Preferences.Key<kotlin.String> = stringPreferencesKey("key-string")
                
                  public val KEY_BOOLEAN: Preferences.Key<kotlin.Boolean> = booleanPreferencesKey("key-boolean")
                
                  public val KEY_FLOAT: Preferences.Key<kotlin.Float> = floatPreferencesKey("key-float")
                
                  public val KEY_LONG: Preferences.Key<kotlin.Long> = longPreferencesKey("key-long")
                }
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesKeys.kt").trimIndent().replaceTab()
        )

        // SecurityPreferencesImpl
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.core.DataStore
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.edit
                import kotlinx.coroutines.flow.Flow
                import kotlinx.coroutines.flow.first
                import kotlinx.coroutines.flow.map
                
                internal class SecurityPreferencesImpl(
                  private val preferencesStore: DataStore<Preferences>,
                ) : SecurityPreferences {
                  public override fun getInt(): Flow<Int> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_INT] ?: 0
                  }
                
                  public override suspend fun getIntValue(): Int = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_INT] ?: 0
                  }
                  .first()
                
                  public override suspend fun setInt(`value`: Int) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_INT] = value
                        }
                  }
                
                  public override fun getDouble(): Flow<Double> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_DOUBLE] ?: 0.0
                  }
                
                  public override suspend fun setDouble(`value`: Double) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_DOUBLE] = value
                        }
                  }
                
                  public override fun getString(): Flow<String> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_STRING] ?: ""
                  }
                
                  public override suspend fun setString(`value`: String) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_STRING] = value
                        }
                  }
                
                  public override fun getBoolean(): Flow<Boolean> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_BOOLEAN] ?: false
                  }
                
                  public override suspend fun setBoolean(`value`: Boolean) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_BOOLEAN] = value
                        }
                  }
                
                  public override fun getFloat(): Flow<Float> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_FLOAT] ?: 0.0F
                  }
                
                  public override suspend fun setFloat(`value`: Float) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_FLOAT] = value
                        }
                  }
                
                  public override fun getLong(): Flow<Long> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_LONG] ?: 0L
                  }
                
                  public override suspend fun setLong(`value`: Long) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_LONG] = value
                        }
                  }
                
                  public override suspend fun clearAll() {
                    preferencesStore.edit {
                        it.clear()
                        }
                  }
                }
                
                public fun DataStore<Preferences>.generateSecurityPreferences(): SecurityPreferences =
                    SecurityPreferencesImpl(preferencesStore = this)
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesImpl.kt").trimIndent().replaceTab()
        )

        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
    }

    @Test
    fun `test NonSecurePreferences - default value`() {
        val securityClass = SourceFile.kotlin(
            "UsefulSecurity.kt",
            """
            package tech.thdev.useful.encrypted.data.store.preferences.security

            interface UsefulSecurity {

                /**
                 * Data to encryptData
                 */
                fun encryptData(data: String): String

                /**
                 * encryptData to decrypt
                 */
                fun decryptData(encryptData: String): String
            }
        """.trimIndent()
        )

        val securityPreferences = SourceFile.kotlin(
            "SecurityPreferences.kt",
            """
                package tech.thdev.samplepreference.repository
                
                import kotlinx.coroutines.flow.Flow
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue
                
                @UsefulPreferences(disableSecure = true)
                interface SecurityPreferences {
                
                    @GetValue(KEY_INT, "30")
                    fun getInt(): Flow<Int>
                
                    @GetValue(KEY_INT, "3")
                    suspend fun getIntValue(): Int
                
                    @SetValue(KEY_INT)
                    suspend fun setInt(value: Int)
                
                    @GetValue(KEY_DOUBLE, "20.03")
                    fun getDouble(): Flow<Double>
                
                    @SetValue(KEY_DOUBLE)
                    suspend fun setDouble(value: Double)
                
                    @GetValue(KEY_STRING, "default value")
                    fun getString(): Flow<String>
                
                    @SetValue(KEY_STRING)
                    suspend fun setString(value: String)
                
                    @GetValue(KEY_BOOLEAN, "true")
                    fun getBoolean(): Flow<Boolean>
                
                    @SetValue(KEY_BOOLEAN)
                    suspend fun setBoolean(value: Boolean)
                
                    @GetValue(KEY_FLOAT, "130.0")
                    fun getFloat(): Flow<Float>
                
                    @SetValue(KEY_FLOAT)
                    suspend fun setFloat(value: Float)
                
                    @GetValue(KEY_LONG, "60")
                    fun getLong(): Flow<Long>
                
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
            """.trimIndent()
        )

        val compilationResult = compile(securityClass, securityPreferences)

        // SecurityPreferencesKeys
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.Preferences.Key
                import androidx.datastore.preferences.core.booleanPreferencesKey
                import androidx.datastore.preferences.core.doublePreferencesKey
                import androidx.datastore.preferences.core.floatPreferencesKey
                import androidx.datastore.preferences.core.intPreferencesKey
                import androidx.datastore.preferences.core.longPreferencesKey
                import androidx.datastore.preferences.core.stringPreferencesKey
                
                internal object SecurityPreferencesKeys {
                  public val KEY_INT: Preferences.Key<kotlin.Int> = intPreferencesKey("key-int")
                
                  public val KEY_DOUBLE: Preferences.Key<kotlin.Double> = doublePreferencesKey("key-double")
                
                  public val KEY_STRING: Preferences.Key<kotlin.String> = stringPreferencesKey("key-string")
                
                  public val KEY_BOOLEAN: Preferences.Key<kotlin.Boolean> = booleanPreferencesKey("key-boolean")
                
                  public val KEY_FLOAT: Preferences.Key<kotlin.Float> = floatPreferencesKey("key-float")
                
                  public val KEY_LONG: Preferences.Key<kotlin.Long> = longPreferencesKey("key-long")
                }
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesKeys.kt").trimIndent().replaceTab()
        )

        // SecurityPreferencesImpl
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.core.DataStore
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.edit
                import kotlinx.coroutines.flow.Flow
                import kotlinx.coroutines.flow.first
                import kotlinx.coroutines.flow.map
                
                internal class SecurityPreferencesImpl(
                  private val preferencesStore: DataStore<Preferences>,
                ) : SecurityPreferences {
                  public override fun getInt(): Flow<Int> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_INT] ?: 30
                  }
                
                  public override suspend fun getIntValue(): Int = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_INT] ?: 3
                  }
                  .first()
                
                  public override suspend fun setInt(`value`: Int) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_INT] = value
                        }
                  }
                
                  public override fun getDouble(): Flow<Double> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_DOUBLE] ?: 20.03
                  }
                
                  public override suspend fun setDouble(`value`: Double) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_DOUBLE] = value
                        }
                  }
                
                  public override fun getString(): Flow<String> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_STRING] ?: "default value"
                  }
                
                  public override suspend fun setString(`value`: String) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_STRING] = value
                        }
                  }
                
                  public override fun getBoolean(): Flow<Boolean> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_BOOLEAN] ?: true
                  }
                
                  public override suspend fun setBoolean(`value`: Boolean) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_BOOLEAN] = value
                        }
                  }
                
                  public override fun getFloat(): Flow<Float> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_FLOAT] ?: 130.0
                  }
                
                  public override suspend fun setFloat(`value`: Float) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_FLOAT] = value
                        }
                  }
                
                  public override fun getLong(): Flow<Long> = preferencesStore.data
                  .map {
                  it[SecurityPreferencesKeys.KEY_LONG] ?: 60
                  }
                
                  public override suspend fun setLong(`value`: Long) {
                    preferencesStore.edit {
                        it[SecurityPreferencesKeys.KEY_LONG] = value
                        }
                  }
                
                  public override suspend fun clearAll() {
                    preferencesStore.edit {
                        it.clear()
                        }
                  }
                }
                
                public fun DataStore<Preferences>.generateSecurityPreferences(): SecurityPreferences =
                    SecurityPreferencesImpl(preferencesStore = this)
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesImpl.kt").trimIndent().replaceTab()
        )

        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
    }

    @Test
    fun `test securityPreferences`() {
        val securityClass = SourceFile.kotlin(
            "UsefulSecurity.kt",
            """
            package tech.thdev.useful.encrypted.data.store.preferences.security

            interface UsefulSecurity {

                /**
                 * Data to encryptData
                 */
                fun encryptData(data: String): String

                /**
                 * encryptData to decrypt
                 */
                fun decryptData(encryptData: String): String
            }
        """.trimIndent()
        )

        val securityPreferences = SourceFile.kotlin(
            "SecurityPreferences.kt",
            """
                package tech.thdev.samplepreference.repository
                
                import kotlinx.coroutines.flow.Flow
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue
                
                @UsefulPreferences
                interface SecurityPreferences {
                
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
            """.trimIndent()
        )

        val compilationResult = compile(securityClass, securityPreferences)

        // SecurityPreferencesKeys
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.Preferences.Key
                import androidx.datastore.preferences.core.stringPreferencesKey
                
                internal object SecurityPreferencesKeys {
                  public val KEY_INT: Preferences.Key<kotlin.String> = stringPreferencesKey("key-int")
                
                  public val KEY_DOUBLE: Preferences.Key<kotlin.String> = stringPreferencesKey("key-double")
                
                  public val KEY_STRING: Preferences.Key<kotlin.String> = stringPreferencesKey("key-string")
                
                  public val KEY_BOOLEAN: Preferences.Key<kotlin.String> = stringPreferencesKey("key-boolean")
                
                  public val KEY_FLOAT: Preferences.Key<kotlin.String> = stringPreferencesKey("key-float")
                
                  public val KEY_LONG: Preferences.Key<kotlin.String> = stringPreferencesKey("key-long")
                }
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesKeys.kt").trimIndent().replaceTab()
        )

        // SecurityPreferencesImpl
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.core.DataStore
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.edit
                import kotlinx.coroutines.flow.Flow
                import kotlinx.coroutines.flow.first
                import tech.thdev.useful.encrypted.`data`.store.preferences.extensions.editEncrypt
                import tech.thdev.useful.encrypted.`data`.store.preferences.extensions.mapDecrypt
                import tech.thdev.useful.encrypted.`data`.store.preferences.security.UsefulSecurity
                
                internal class SecurityPreferencesImpl(
                  private val usefulSecurity: UsefulSecurity,
                  private val preferencesStore: DataStore<Preferences>,
                ) : SecurityPreferences {
                  public override fun getInt(): Flow<Int> = preferencesStore.data
                  .mapDecrypt<Int>(
                      usefulSecurity = usefulSecurity,
                      type = Int::class,
                      defaultValue = "0",
                  ) {
                  it[SecurityPreferencesKeys.KEY_INT]
                  }
                
                  public override suspend fun getIntValue(): Int = preferencesStore.data
                  .mapDecrypt<Int>(
                      usefulSecurity = usefulSecurity,
                      type = Int::class,
                      defaultValue = "0",
                  ) {
                  it[SecurityPreferencesKeys.KEY_INT]
                  }
                  .first()
                
                  public override suspend fun setInt(`value`: Int) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_INT] = encrypted
                        }
                  }
                
                  public override fun getDouble(): Flow<Double> = preferencesStore.data
                  .mapDecrypt<Double>(
                      usefulSecurity = usefulSecurity,
                      type = Double::class,
                      defaultValue = "0.0",
                  ) {
                  it[SecurityPreferencesKeys.KEY_DOUBLE]
                  }
                
                  public override suspend fun setDouble(`value`: Double) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_DOUBLE] = encrypted
                        }
                  }
                
                  public override fun getString(): Flow<String> = preferencesStore.data
                  .mapDecrypt<String>(
                      usefulSecurity = usefulSecurity,
                      type = String::class,
                      defaultValue = "",
                  ) {
                  it[SecurityPreferencesKeys.KEY_STRING]
                  }
                
                  public override suspend fun setString(`value`: String) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_STRING] = encrypted
                        }
                  }
                
                  public override fun getBoolean(): Flow<Boolean> = preferencesStore.data
                  .mapDecrypt<Boolean>(
                      usefulSecurity = usefulSecurity,
                      type = Boolean::class,
                      defaultValue = "false",
                  ) {
                  it[SecurityPreferencesKeys.KEY_BOOLEAN]
                  }
                
                  public override suspend fun setBoolean(`value`: Boolean) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_BOOLEAN] = encrypted
                        }
                  }
                
                  public override fun getFloat(): Flow<Float> = preferencesStore.data
                  .mapDecrypt<Float>(
                      usefulSecurity = usefulSecurity,
                      type = Float::class,
                      defaultValue = "0.0F",
                  ) {
                  it[SecurityPreferencesKeys.KEY_FLOAT]
                  }
                
                  public override suspend fun setFloat(`value`: Float) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_FLOAT] = encrypted
                        }
                  }
                
                  public override fun getLong(): Flow<Long> = preferencesStore.data
                  .mapDecrypt<Long>(
                      usefulSecurity = usefulSecurity,
                      type = Long::class,
                      defaultValue = "0",
                  ) {
                  it[SecurityPreferencesKeys.KEY_LONG]
                  }
                
                  public override suspend fun setLong(`value`: Long) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_LONG] = encrypted
                        }
                  }
                
                  public override suspend fun clearAll() {
                    preferencesStore.edit {
                        it.clear()
                        }
                  }
                }
                
                public fun DataStore<Preferences>.generateSecurityPreferences(usefulSecurity: UsefulSecurity):
                    SecurityPreferences = SecurityPreferencesImpl(preferencesStore = this, usefulSecurity =
                    usefulSecurity)
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesImpl.kt").trimIndent().replaceTab()
        )

        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
    }

    @Test
    fun `test securityPreferences - default value`() {
        val securityClass = SourceFile.kotlin(
            "UsefulSecurity.kt",
            """
            package tech.thdev.useful.encrypted.data.store.preferences.security

            interface UsefulSecurity {

                /**
                 * Data to encryptData
                 */
                fun encryptData(data: String): String

                /**
                 * encryptData to decrypt
                 */
                fun decryptData(encryptData: String): String
            }
        """.trimIndent()
        )

        val securityPreferences = SourceFile.kotlin(
            "SecurityPreferences.kt",
            """
                package tech.thdev.samplepreference.repository
                
                import kotlinx.coroutines.flow.Flow
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
                import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue
                
                @UsefulPreferences
                interface SecurityPreferences {
                
                    @GetValue(KEY_INT, "123")
                    fun getInt(): Flow<Int>
                
                    @GetValue(KEY_INT, "345")
                    suspend fun getIntValue(): Int
                
                    @SetValue(KEY_INT)
                    suspend fun setInt(value: Int)
                
                    @GetValue(KEY_DOUBLE, "122.1234")
                    fun getDouble(): Flow<Double>
                
                    @SetValue(KEY_DOUBLE)
                    suspend fun setDouble(value: Double)
                
                    @GetValue(KEY_STRING, "string value")
                    fun getString(): Flow<String>
                
                    @SetValue(KEY_STRING)
                    suspend fun setString(value: String)
                
                    @GetValue(KEY_BOOLEAN, "true")
                    fun getBoolean(): Flow<Boolean>
                
                    @SetValue(KEY_BOOLEAN)
                    suspend fun setBoolean(value: Boolean)
                
                    @GetValue(KEY_FLOAT, "0.1234")
                    fun getFloat(): Flow<Float>
                
                    @SetValue(KEY_FLOAT)
                    suspend fun setFloat(value: Float)
                
                    @GetValue(KEY_LONG, "5151")
                    fun getLong(): Flow<Long>
                
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
            """.trimIndent()
        )

        val compilationResult = compile(securityClass, securityPreferences)

        // SecurityPreferencesKeys
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.Preferences.Key
                import androidx.datastore.preferences.core.stringPreferencesKey
                
                internal object SecurityPreferencesKeys {
                  public val KEY_INT: Preferences.Key<kotlin.String> = stringPreferencesKey("key-int")
                
                  public val KEY_DOUBLE: Preferences.Key<kotlin.String> = stringPreferencesKey("key-double")
                
                  public val KEY_STRING: Preferences.Key<kotlin.String> = stringPreferencesKey("key-string")
                
                  public val KEY_BOOLEAN: Preferences.Key<kotlin.String> = stringPreferencesKey("key-boolean")
                
                  public val KEY_FLOAT: Preferences.Key<kotlin.String> = stringPreferencesKey("key-float")
                
                  public val KEY_LONG: Preferences.Key<kotlin.String> = stringPreferencesKey("key-long")
                }
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesKeys.kt").trimIndent().replaceTab()
        )

        // SecurityPreferencesImpl
        Assertions.assertEquals(
            """
                package tech.thdev.samplepreference.repository
                
                import androidx.datastore.core.DataStore
                import androidx.datastore.preferences.core.Preferences
                import androidx.datastore.preferences.core.edit
                import kotlinx.coroutines.flow.Flow
                import kotlinx.coroutines.flow.first
                import tech.thdev.useful.encrypted.`data`.store.preferences.extensions.editEncrypt
                import tech.thdev.useful.encrypted.`data`.store.preferences.extensions.mapDecrypt
                import tech.thdev.useful.encrypted.`data`.store.preferences.security.UsefulSecurity
                
                internal class SecurityPreferencesImpl(
                  private val usefulSecurity: UsefulSecurity,
                  private val preferencesStore: DataStore<Preferences>,
                ) : SecurityPreferences {
                  public override fun getInt(): Flow<Int> = preferencesStore.data
                  .mapDecrypt<Int>(
                      usefulSecurity = usefulSecurity,
                      type = Int::class,
                      defaultValue = "123",
                  ) {
                  it[SecurityPreferencesKeys.KEY_INT]
                  }
                
                  public override suspend fun getIntValue(): Int = preferencesStore.data
                  .mapDecrypt<Int>(
                      usefulSecurity = usefulSecurity,
                      type = Int::class,
                      defaultValue = "345",
                  ) {
                  it[SecurityPreferencesKeys.KEY_INT]
                  }
                  .first()
                
                  public override suspend fun setInt(`value`: Int) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_INT] = encrypted
                        }
                  }
                
                  public override fun getDouble(): Flow<Double> = preferencesStore.data
                  .mapDecrypt<Double>(
                      usefulSecurity = usefulSecurity,
                      type = Double::class,
                      defaultValue = "122.1234",
                  ) {
                  it[SecurityPreferencesKeys.KEY_DOUBLE]
                  }
                
                  public override suspend fun setDouble(`value`: Double) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_DOUBLE] = encrypted
                        }
                  }
                
                  public override fun getString(): Flow<String> = preferencesStore.data
                  .mapDecrypt<String>(
                      usefulSecurity = usefulSecurity,
                      type = String::class,
                      defaultValue = "string value",
                  ) {
                  it[SecurityPreferencesKeys.KEY_STRING]
                  }
                
                  public override suspend fun setString(`value`: String) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_STRING] = encrypted
                        }
                  }
                
                  public override fun getBoolean(): Flow<Boolean> = preferencesStore.data
                  .mapDecrypt<Boolean>(
                      usefulSecurity = usefulSecurity,
                      type = Boolean::class,
                      defaultValue = "true",
                  ) {
                  it[SecurityPreferencesKeys.KEY_BOOLEAN]
                  }
                
                  public override suspend fun setBoolean(`value`: Boolean) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_BOOLEAN] = encrypted
                        }
                  }
                
                  public override fun getFloat(): Flow<Float> = preferencesStore.data
                  .mapDecrypt<Float>(
                      usefulSecurity = usefulSecurity,
                      type = Float::class,
                      defaultValue = "0.1234",
                  ) {
                  it[SecurityPreferencesKeys.KEY_FLOAT]
                  }
                
                  public override suspend fun setFloat(`value`: Float) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_FLOAT] = encrypted
                        }
                  }
                
                  public override fun getLong(): Flow<Long> = preferencesStore.data
                  .mapDecrypt<Long>(
                      usefulSecurity = usefulSecurity,
                      type = Long::class,
                      defaultValue = "5151",
                  ) {
                  it[SecurityPreferencesKeys.KEY_LONG]
                  }
                
                  public override suspend fun setLong(`value`: Long) {
                    preferencesStore.editEncrypt(usefulSecurity, value) { preferences, encrypted -> 
                        preferences[SecurityPreferencesKeys.KEY_LONG] = encrypted
                        }
                  }
                
                  public override suspend fun clearAll() {
                    preferencesStore.edit {
                        it.clear()
                        }
                  }
                }
                
                public fun DataStore<Preferences>.generateSecurityPreferences(usefulSecurity: UsefulSecurity):
                    SecurityPreferences = SecurityPreferencesImpl(preferencesStore = this, usefulSecurity =
                    usefulSecurity)
            """.trimIndent(),
            compilationResult.sourceFor("SecurityPreferencesImpl.kt").trimIndent().replaceTab()
        )

        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
    }

    private fun String.replaceTab(): String =
        replace("\t", "    ")

    private fun compile(vararg source: SourceFile) = KotlinCompilation().apply {
        sources = source.toList()
        symbolProcessorProviders = listOf(EncryptedDataStorePreferencesProcessorProvider())
        workingDir = tempDir.toFile()
        inheritClassPath = true
        verbose = false
        messageOutputStream = System.out
    }.compile()

    companion object {
        @TempDir
        lateinit var tempDir: Path
    }

    private fun KotlinCompilation.Result.sourceFor(fileName: String): String {
        return kspGeneratedSources().find { it.name == fileName }
            ?.readText()
            ?: throw IllegalArgumentException("Could not find file $fileName in ${kspGeneratedSources()}")
    }

    private fun KotlinCompilation.Result.kspGeneratedSources(): List<File> {
        val kspWorkingDir = workingDir.resolve("ksp")
        val kspGeneratedDir = kspWorkingDir.resolve("sources")
        val kotlinGeneratedDir = kspGeneratedDir.resolve("kotlin")
        val javaGeneratedDir = kspGeneratedDir.resolve("java")
        return kotlinGeneratedDir.walk().toList() +
                javaGeneratedDir.walk().toList()
    }

    private val KotlinCompilation.Result.workingDir: File
        get() = checkNotNull(outputDirectory.parentFile)
}
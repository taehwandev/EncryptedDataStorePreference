## Summary

Android Encrypted DataStorePreference.

Include Data Encrypt in Android DataStorePreference. This library use KSP(ksp version 1.7.10-1.0.6).

## Download

Use gradle

```groovy
ksp "tech.thdev:useful-encrypted-data-store-preferences-ksp:$lastVersion"
implementation "tech.thdev:useful-encrypted-data-store-preferences-ksp-annotations:$lastVersion"
implementation "tech.thdev:useful-encrypted-data-store-preferences-security:$lastVersion"
```

Use gradle.kts

```kotlin
plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    ksp("tech.thdev:useful-encrypted-data-store-preferences-ksp:$lastVersion")
    implementation("tech.thdev:useful-encrypted-data-store-preferences-ksp-annotations:$lastVersion")
    implementation("tech.thdev:useful-encrypted-data-store-preferences-security:$lastVersion")
}
```

Release version are available in [Sonatyp's repository.](https://search.maven.org/search?q=tech.thdev)

## Use Code

```kotlin
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

    companion object {

        private const val KEY_INT = "key-int"
        private const val KEY_DOUBLE = "key-double"
        private const val KEY_STRING = "key-string"
        private const val KEY_BOOLEAN = "key-boolean"
        private const val KEY_FLOAT = "key-float"
        private const val KEY_LONG = "key-long"
    }
}
```

and rebuild.

use Activity or application

Use the provided security or implement UsefulSecurity inheritance.

```kotlin
class SampleActivity {
    private val Context.dataStore by preferencesDataStore(name = "security-preference")

    private val securityPreference: SecurityPreferences by lazy {
        dataStore.generateSecurityPreferences(generateUsefulSecurity())
    }
    
    // SetValue
    coroutineScope.launch {
        securityPreference.setInt(++count)
    }
    
    // GetValue -- return Flow
    securityPreference.getInt()
    .flowOn(Dispatchers.IO)
    .onEach {
        Toast.makeText(this@MainActivity, "Current Int $it", Toast.LENGTH_SHORT).show()
    }
    .catch {
        it.printStackTrace()
    }
    .launchIn(this)
    
    // GetValue -- suspend function
    coroutineScope.launch {
        securityPreference.getIntValue()
    }
}
```

## Custom Security

```kotlin
implementation("tech.thdev:useful-encrypted-data-store-preferences-security:$lastVersion")
```

Implement UsefulSecurity inheritance.

```kotlin
class CustomSecurityImpl : UsefulSecurity {
    
    override fun encryptData(data: String): String {
        TODO("Not yet implemented")
    }

    override fun decryptData(encryptData: String): String {
        TODO("Not yet implemented")
    }
}
```

## The KSP path must be specified as required.

module gradle.

```kotlin
buildTypes {
    sourceSets.getByName("debug") {
        kotlin.srcDir("build/generated/ksp/debug/kotlin")
    }
    sourceSets.getByName("release") {
        kotlin.srcDir("build/generated/ksp/release/kotlin")
    }
}
```

## Result
<img width="130" alt="sample" src="https://user-images.githubusercontent.com/2144231/182009046-2b11d0fc-5acb-4b46-8d87-94712d958ea4.png">


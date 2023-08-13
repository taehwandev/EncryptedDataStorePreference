## 1.9.0-1.0.13-1.2.0-alpha02

- Performance improvement
  - Split keystore value.
- Change ecb.
  - Fix app First start decrypt fail.

## 1.9.0-1.0.13-1.2.0-alpha01

- Secure change. Use AndroidKeyStore

## 1.7.21-1.0.8-1.1.0-alpha01

- Add defaultValue
- Default using String value.
  - Int -> "0"
  - Double -> "0.0"
  - String -> "message"
  - Boolean -> "true" or "false"
  - Float -> "0.0"
  - Long -> "0"

## 1.7.21-1.0.8-1.0.0

- Library update.

## 1.0.0-alpha03

- New annotation add.
- @ClearValues - Use suspend function
 ```kotlin
@ClearValues
suspend fun clearAll()
```
- security map event change.
  - empty value stream default

## 1.0.0-alpha01

- DataStorePreferences ksp module
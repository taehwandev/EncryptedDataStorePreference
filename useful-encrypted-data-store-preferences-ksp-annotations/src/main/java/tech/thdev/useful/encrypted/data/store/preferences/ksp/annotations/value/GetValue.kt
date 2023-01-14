package tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class GetValue(
    /**
     * DataStore key
     */
    @Suppress("unused") val key: String,
    /**
     * default value option.
     * only string.
     *
     * Int -> "0"
     * Double -> "0.0"
     * String -> "message"
     * Boolean -> "true" or "false"
     * Float -> "0.0"
     * Long -> "0"
     */
    @Suppress("unused") val defaultValue: String = "",
)
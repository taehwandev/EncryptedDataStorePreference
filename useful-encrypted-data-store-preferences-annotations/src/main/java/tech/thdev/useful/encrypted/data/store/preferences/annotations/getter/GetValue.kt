package tech.thdev.useful.encrypted.data.store.preferences.annotations.getter

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class GetValue(val key: String)
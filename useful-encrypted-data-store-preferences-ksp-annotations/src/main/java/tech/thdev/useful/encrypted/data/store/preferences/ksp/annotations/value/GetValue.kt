package tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class GetValue(val key: String)
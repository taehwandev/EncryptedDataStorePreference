package tech.thdev.useful.encrypted.data.store.preferences.annotations.setter

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
annotation class SetValue(val key: String)
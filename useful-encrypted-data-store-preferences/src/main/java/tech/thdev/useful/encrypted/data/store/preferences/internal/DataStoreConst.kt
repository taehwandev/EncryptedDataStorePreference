package tech.thdev.useful.encrypted.data.store.preferences.internal

import com.squareup.kotlinpoet.asClassName
import tech.thdev.useful.encrypted.data.store.preferences.annotations.UsefulPreferences

internal object DataStoreConst {

    val ANNOTATION_USEFUL_PREFERENCES = UsefulPreferences::class.asClassName()
}
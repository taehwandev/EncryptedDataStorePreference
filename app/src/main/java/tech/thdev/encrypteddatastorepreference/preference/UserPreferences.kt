package tech.thdev.encrypteddatastorepreference.preference

import kotlinx.coroutines.flow.Flow
import tech.thdev.useful.encrypted.data.store.preferences.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.annotations.getter.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.annotations.setter.SetValue

@UsefulPreferences
interface UserPreferences {

//    @GetValue(KEY_USER_ID)
//    abstract fun getUserId(): Flow<String>

//    @SetValue(KEY_USER_ID)
//    abstract suspend fun setUserId(userId: String)

    companion object {

        private const val KEY_USER_ID = "key-user-id"
    }
}
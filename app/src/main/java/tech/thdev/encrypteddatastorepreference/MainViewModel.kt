package tech.thdev.encrypteddatastorepreference

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.samplepreference.repository.SecurityPreferences

class MainViewModel(
    private val securityPreference: SecurityPreferences,
) : ViewModel() {

    val intValue = mutableStateOf(0)
    val doubleValue = mutableStateOf(0.0)
    val floatValue = mutableStateOf(0.0f)
    val stringValue = mutableStateOf("")
    val booleanValue = mutableStateOf(false)
    val longValue = mutableStateOf(0L)

    init {
        securityPreference.flowInt()
            .onEach {
                Log.d("DataStore", "Current Int $it")
                intValue.value = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowDouble()
            .onEach {
                Log.d("DataStore", "Current Double $it")
                doubleValue.value = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowString()
            .onEach {
                Log.d("DataStore", "Current String $it")
                stringValue.value = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowBoolean()
            .onEach {
                Log.d("DataStore", "Current Boolean $it")
                booleanValue.value = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowFloat()
            .onEach {
                Log.d("DataStore", "Current Float $it")
                floatValue.value = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowLong()
            .onEach {
                Log.d("DataStore", "Current Long $it")
                longValue.value = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    fun intValueChange() = viewModelScope.launch {
        securityPreference.setInt(intValue.value++)
    }

    fun doubleValueChange() = viewModelScope.launch {
        securityPreference.setDouble(doubleValue.value++)
    }

    fun floatValueChange() = viewModelScope.launch {
        securityPreference.setFloat(floatValue.value++)
    }

    fun stringValueChange() = viewModelScope.launch {
        securityPreference.setString("New value ${intValue.value}")
    }

    fun booleanValueChange() = viewModelScope.launch {
        securityPreference.setBoolean(booleanValue.value.not())
    }

    fun longValueChange() = viewModelScope.launch {
        securityPreference.setLong(longValue.value++)
    }

    fun clear() = viewModelScope.launch {
        securityPreference.clearAll()
    }
}
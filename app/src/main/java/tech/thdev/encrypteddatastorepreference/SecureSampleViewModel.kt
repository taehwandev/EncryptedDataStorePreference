package tech.thdev.encrypteddatastorepreference

import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.samplepreference.repository.SecurePreferences

class SecureSampleViewModel(
    private val securityPreference: SecurePreferences,
) : ViewModel() {

    val intValue = mutableIntStateOf(0)
    val doubleValue = mutableDoubleStateOf(0.0)
    val floatValue = mutableFloatStateOf(0.0f)
    val stringValue = mutableStateOf("")
    val booleanValue = mutableStateOf(false)
    val longValue = mutableLongStateOf(0L)

    init {
        securityPreference.flowInt()
            .onEach {
                Log.d("DataStore", "Current Int $it")
                intValue.intValue = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowDouble()
            .onEach {
                Log.d("DataStore", "Current Double $it")
                doubleValue.doubleValue = it
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
                floatValue.floatValue = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)

        securityPreference.flowLong()
            .onEach {
                Log.d("DataStore", "Current Long $it")
                longValue.longValue = it
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    fun intValueChange() = viewModelScope.launch {
        securityPreference.setInt(++intValue.intValue)
    }

    fun doubleValueChange() = viewModelScope.launch {
        securityPreference.setDouble(++doubleValue.doubleValue)
    }

    fun floatValueChange() = viewModelScope.launch {
        securityPreference.setFloat(++floatValue.floatValue)
    }

    fun stringValueChange() = viewModelScope.launch {
        securityPreference.setString("New value ${intValue.intValue}")
    }

    fun booleanValueChange() = viewModelScope.launch {
        securityPreference.setBoolean(booleanValue.value.not())
    }

    fun longValueChange() = viewModelScope.launch {
        securityPreference.setLong(++longValue.longValue)
    }

    fun clear() = viewModelScope.launch {
        securityPreference.clearAll()
    }
}
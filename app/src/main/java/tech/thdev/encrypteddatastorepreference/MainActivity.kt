package tech.thdev.encrypteddatastorepreference

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.thdev.encrypteddatastorepreference.compose.MainContainer
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme
import tech.thdev.samplepreference.repository.SecurityPreferences
import tech.thdev.samplepreference.repository.generateSecurityPreferences
import tech.thdev.useful.encrypted.data.store.preferences.security.generateUsefulSecurity

class MainActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "security-preference")

    private val securityPreference: SecurityPreferences by lazy {
        dataStore.generateSecurityPreferences(generateUsefulSecurity())
    }

    @Suppress("UNCHECKED_CAST")
    internal val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(securityPreference) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLocalProvider {
                EncryptedDataStorePreferenceTheme {
                    MainContainer()
                }
            }
        }
    }
}
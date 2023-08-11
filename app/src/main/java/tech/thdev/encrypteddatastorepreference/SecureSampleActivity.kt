package tech.thdev.encrypteddatastorepreference

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.thdev.encrypteddatastorepreference.compose.SecureSampleScreen
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme
import tech.thdev.samplepreference.repository.SecurePreferences
import tech.thdev.samplepreference.repository.generateSecurePreferences
import tech.thdev.useful.encrypted.data.store.preferences.security.generateUsefulSecurity

class SecureSampleActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "security-preference")

    private val securityPreference: SecurePreferences by lazy {
        dataStore.generateSecurePreferences(
            generateUsefulSecurity(
                keyStoreAlias = packageName,
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    internal val viewModel by viewModels<SecureSampleViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SecureSampleViewModel(securityPreference) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLocalProvider {
                EncryptedDataStorePreferenceTheme {
                    SecureSampleScreen()
                }
            }
        }
    }
}
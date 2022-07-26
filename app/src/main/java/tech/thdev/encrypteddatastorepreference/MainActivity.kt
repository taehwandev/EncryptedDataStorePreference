package tech.thdev.encrypteddatastorepreference

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.encrypteddatastorepreference.preference.UserPreferencesImpl
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme

class MainActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "user-preference")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreference = UserPreferencesImpl(dataStore)

                val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            userPreference.getUserId()
                .onEach {
                    Toast.makeText(this@MainActivity, "UserId $it", Toast.LENGTH_SHORT).show()
                }
                .flowOn(Dispatchers.Main)
                .launchIn(this)
        }

        var count = 0
        coroutineScope.launch {
            (0..1).forEach { _ ->
                userPreference.setUserId("UserId ${++count}")
                delay(1_000L)
            }
        }

        setContent {
            EncryptedDataStorePreferenceTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EncryptedDataStorePreferenceTheme {
        Greeting("Android")
    }
}
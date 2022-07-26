package tech.thdev.encrypteddatastorepreference

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme
import tech.thdev.samplepreference.SamplePreferences
import tech.thdev.samplepreference.generateSamplePreferences

class MainActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "user-preference")

    private val samplePreference: SamplePreferences by lazy {
        dataStore.generateSamplePreferences()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptedDataStorePreferenceTheme {
                LaunchedEffect(key1 = samplePreference) {
                    samplePreference.getString()
                        .onEach {
                            Toast.makeText(this@MainActivity, "UserId $it", Toast.LENGTH_SHORT).show()
                        }
                        .flowOn(Dispatchers.Main)
                        .launchIn(this)
                }

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val coroutineScope = rememberCoroutineScope()
                    MainContainer("Android") {
                        coroutineScope.launch {
                            samplePreference.setString("String test current count : ${++count}")
                        }
                    }
                }
            }
        }
    }
}

private var count = 0

@Composable
internal fun MainContainer(
    name: String,
    buttonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(text = "Hello $name!")
        Button(onClick = buttonClick) {
            Text(text = "Preference test")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainContainer() {
    EncryptedDataStorePreferenceTheme {
        MainContainer("Android") {}
    }
}
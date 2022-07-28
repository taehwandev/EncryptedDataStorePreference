package tech.thdev.encrypteddatastorepreference

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.encrypteddatastorepreference.preference.Sample
import tech.thdev.encrypteddatastorepreference.preference.SampleImpl
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme

class MainActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "sample-preference")

    private val samplePreference: Sample by lazy {
        SampleImpl(
            dataStore,
        )
    }

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptedDataStorePreferenceTheme {
                LaunchedEffect(key1 = samplePreference) {
                    samplePreference.getInt()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current Int $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)

                    samplePreference.getString()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "UserId $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Encrypted Preference test",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        TestButton(
                            text = "Int type",
                            onClick = {
                                samplePreference.setInt(++count)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Double type",
                            onClick = {
                                samplePreference.setDouble((++count).toDouble())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "String type",
                            onClick = {
                                samplePreference.setString("Current data ${++count}")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Boolean type",
                            onClick = {
                                samplePreference.setBoolean(count % 2 == 0)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Long type",
                            onClick = {
                                samplePreference.setLong((++count).toLong())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Float type",
                            onClick = {
                                samplePreference.setFloat((++count).toFloat())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun TestButton(
    modifier: Modifier,
    text: String,
    onClick: suspend () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            coroutineScope.launch {
                onClick()
            }
        },
        modifier = modifier
    ) {
        Text(text = text)
    }
}
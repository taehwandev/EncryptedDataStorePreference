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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme
import tech.thdev.samplepreference.repository.SecurityPreferences
import tech.thdev.samplepreference.repository.generateSecurityPreferences
import tech.thdev.useful.encrypted.data.store.preferences.security.generateUsefulSecurity

class MainActivity : ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "security-preference")

    private val securityPreference: SecurityPreferences by lazy {
        dataStore.generateSecurityPreferences(generateUsefulSecurity())
    }

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptedDataStorePreferenceTheme {
                LaunchedEffect(key1 = securityPreference) {
                    securityPreference.getInt()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current Int $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)

                    securityPreference.getDouble()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current Double $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)

                    securityPreference.getString()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current String $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)

                    securityPreference.getBoolean()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current Boolean $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)

                    securityPreference.getFloat()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current Float $it", Toast.LENGTH_SHORT).show()
                        }
                        .catch {
                            it.printStackTrace()
                        }
                        .launchIn(this)

                    securityPreference.getLong()
                        .flowOn(Dispatchers.IO)
                        .onEach {
                            Toast.makeText(this@MainActivity, "Current Long $it", Toast.LENGTH_SHORT).show()
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
                        .padding(20.dp)
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
                                securityPreference.setInt(++count)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        )
                        TestButton(
                            text = "Double type",
                            onClick = {
                                securityPreference.setDouble((++count).toDouble())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "String type",
                            onClick = {
                                securityPreference.setString("Current data ${++count}")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Boolean type",
                            onClick = {
                                securityPreference.setBoolean(count % 2 == 0)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Long type",
                            onClick = {
                                securityPreference.setLong((++count).toLong())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Float type",
                            onClick = {
                                securityPreference.setFloat((++count).toFloat())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        TestButton(
                            text = "Clear value",
                            onClick = {
                                securityPreference.clearAll()
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
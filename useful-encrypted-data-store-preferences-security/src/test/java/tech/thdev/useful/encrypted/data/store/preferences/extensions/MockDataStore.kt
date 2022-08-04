package tech.thdev.useful.encrypted.data.store.preferences.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.mock

internal class MockDataStore : DataStore<Preferences> {

    private val preferences = mock<Preferences>()

    private val sharedFlow = MutableStateFlow(preferences)

    override val data: Flow<Preferences> =
        sharedFlow

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val value = transform(preferences)
        sharedFlow.value = value
        return value
    }
}
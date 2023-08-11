package tech.thdev.encrypteddatastorepreference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

private val LocalSecureSampleViewModel: ProvidableCompositionLocal<SecureSampleViewModel?> =
    compositionLocalOf<SecureSampleViewModel?> { null }

internal fun mainViewModelProvider(developViewModel: SecureSampleViewModel) =
    LocalSecureSampleViewModel provides developViewModel

internal val localSecureSampleViewModel: SecureSampleViewModel
    @Composable
    get() = LocalSecureSampleViewModel.current!!
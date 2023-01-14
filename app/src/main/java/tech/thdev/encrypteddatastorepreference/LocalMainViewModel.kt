package tech.thdev.encrypteddatastorepreference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

private val LocalMainViewModel: ProvidableCompositionLocal<MainViewModel?> =
    compositionLocalOf<MainViewModel?> { null }

internal fun mainViewModelProvider(developViewModel: MainViewModel) =
    LocalMainViewModel provides developViewModel

internal val localMainViewModel: MainViewModel
    @Composable
    get() = LocalMainViewModel.current!!
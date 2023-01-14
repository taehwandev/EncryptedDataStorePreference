package tech.thdev.encrypteddatastorepreference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
internal fun MainActivity.ComposeLocalProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        mainViewModelProvider(viewModel),
        content = content
    )
}
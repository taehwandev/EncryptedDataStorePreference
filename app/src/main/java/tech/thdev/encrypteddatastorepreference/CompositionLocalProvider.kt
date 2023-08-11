package tech.thdev.encrypteddatastorepreference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
internal fun SecureSampleActivity.ComposeLocalProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        mainViewModelProvider(viewModel),
        content = content
    )
}
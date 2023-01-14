package tech.thdev.encrypteddatastorepreference.compose.sub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme

@Composable
internal fun TestContainer(
    text: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 10.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = buttonText)
        }
    }
}

@Preview
@Composable
internal fun PreviewTestButton() {
    EncryptedDataStorePreferenceTheme {
        TestContainer(
            text = "Text",
            buttonText = "Button",
            onClick = {},
        )
    }
}
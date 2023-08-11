package tech.thdev.encrypteddatastorepreference.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.thdev.encrypteddatastorepreference.compose.component.TestItem
import tech.thdev.encrypteddatastorepreference.localSecureSampleViewModel
import tech.thdev.encrypteddatastorepreference.ui.theme.EncryptedDataStorePreferenceTheme

@Composable
fun <T> MutableState<T>.asRemember(): State<T> {
    return remember { this }
}

@Composable
internal fun SecureSampleScreen() {
    val intValue by localSecureSampleViewModel.intValue.asRemember()
    val doubleValue by localSecureSampleViewModel.doubleValue.asRemember()
    val floatValue by localSecureSampleViewModel.floatValue.asRemember()
    val stringValue by localSecureSampleViewModel.stringValue.asRemember()
    val booleanValue by localSecureSampleViewModel.booleanValue.asRemember()
    val longValue by localSecureSampleViewModel.longValue.asRemember()

    val viewModel = localSecureSampleViewModel
    SecureSampleScreen(
        intValue = intValue,
        onClickChangeInt = {
            viewModel.intValueChange()
        },
        doubleValue = doubleValue,
        onClickChangeDouble = {
            viewModel.doubleValueChange()
        },
        floatValue = floatValue,
        onClickChangeFloat = {
            viewModel.floatValueChange()
        },
        stringValue = stringValue,
        onClickChangeString = {
            viewModel.stringValueChange()
        },
        booleanValue = booleanValue,
        onClickChangeBoolean = {
            viewModel.booleanValueChange()
        },
        longValue = longValue,
        onClickChangeLong = {
            viewModel.longValueChange()
        },
        onClickClean = {
            viewModel.clear()
        }
    )
}

@Composable
internal fun SecureSampleScreen(
    intValue: Int,
    onClickChangeInt: () -> Unit,
    doubleValue: Double,
    onClickChangeDouble: () -> Unit,
    floatValue: Float,
    onClickChangeFloat: () -> Unit,
    stringValue: String,
    onClickChangeString: () -> Unit,
    booleanValue: Boolean,
    onClickChangeBoolean: () -> Unit,
    longValue: Long,
    onClickChangeLong: () -> Unit,
    onClickClean: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Encrypted Preference test",
            style = TextStyle(fontSize = 20.sp)
        )

        TestItem(
            text = "Int value $intValue",
            buttonText = "Int type",
            onClick = onClickChangeInt,
        )

        TestItem(
            text = "Double value $doubleValue",
            buttonText = "Double type",
            onClick = onClickChangeDouble,
        )

        TestItem(
            text = "String value $stringValue",
            buttonText = "String type",
            onClick = onClickChangeString,
        )

        TestItem(
            text = "Boolean value $booleanValue",
            buttonText = "Boolean type",
            onClick = onClickChangeBoolean,
        )

        TestItem(
            text = "Long value $longValue",
            buttonText = "Long type",
            onClick = onClickChangeLong,
        )

        TestItem(
            text = "Float value $floatValue",
            buttonText = "Float type",
            onClick = onClickChangeFloat,
        )

        Button(
            onClick = onClickClean,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(text = "Clear value")
        }
    }
}

@Preview
@Composable
internal fun PreviewSecureSampleScreen() {
    EncryptedDataStorePreferenceTheme {
        SecureSampleScreen(
            intValue = 0,
            onClickChangeInt = {},
            doubleValue = 0.123,
            onClickChangeDouble = {},
            floatValue = 0.12F,
            onClickChangeFloat = {},
            stringValue = "string value",
            onClickChangeString = {},
            booleanValue = true,
            onClickChangeBoolean = {},
            longValue = 1234,
            onClickChangeLong = {},
            onClickClean = {}
        )
    }
}
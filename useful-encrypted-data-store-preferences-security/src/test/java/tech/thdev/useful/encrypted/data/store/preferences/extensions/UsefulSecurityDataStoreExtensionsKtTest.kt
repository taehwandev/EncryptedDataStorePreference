package tech.thdev.useful.encrypted.data.store.preferences.extensions

import android.os.Build
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.thdev.useful.encrypted.data.store.preferences.security.UsefulSecurityImpl

@Config(sdk = [Build.VERSION_CODES.S])
@RunWith(RobolectricTestRunner::class)
internal class UsefulSecurityDataStoreExtensionsKtTest {

    private val security = UsefulSecurityImpl(useTest = true)
    private val datsStore = MockDataStore()

    @Test
    fun `test int - editEncrypt`() = runTest {
        val mockData = "1234511"
        datsStore.editEncrypt(security, mockData) { _, encrypted ->
            Assertions.assertEquals(mockData, security.decryptData(encrypted))
        }
    }

    @Test
    fun `test editEncrypt - Double`() = runTest {
        val mockData = "1234511.001"
        datsStore.editEncrypt(security, mockData) { _, encrypted ->
            Assertions.assertEquals(mockData, security.decryptData(encrypted))
        }
    }

    @Test
    fun `test editEncrypt - String`() = runTest {
        val mockData = "bbaddd"
        datsStore.editEncrypt(security, mockData) { _, encrypted ->
            Assertions.assertEquals(mockData, security.decryptData(encrypted))
        }
    }

    @Test
    fun `test editEncrypt - Boolean`() = runTest {
        val mockData = "true"
        datsStore.editEncrypt(security, mockData) { _, encrypted ->
            Assertions.assertEquals(mockData, security.decryptData(encrypted))
        }
    }

    @Test
    fun `test editEncrypt - Float`() = runTest {
        val mockData = "123f"
        datsStore.editEncrypt(security, mockData) { _, encrypted ->
            Assertions.assertEquals(mockData, security.decryptData(encrypted))
        }
    }

    @Test
    fun `test editEncrypt - Long`() = runTest {
        val mockData = "123L"
        datsStore.editEncrypt(security, mockData) { _, encrypted ->
            Assertions.assertEquals(mockData, security.decryptData(encrypted))
        }
    }

    @Test
    fun `test mapDecrypt empty data - Int`() = runTest {
        val mockData = ""
        datsStore.data
            .mapDecrypt<Int>(security, Int::class, defaultValue = "0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(0, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt - Int`() = runTest {
        val mockData = "1000"
        datsStore.data
            .mapDecrypt<Int>(security, Int::class, defaultValue = "0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(mockData.toInt(), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt - Double`() = runTest {
        val mockData = "100.0"
        datsStore.data
            .mapDecrypt<Double>(security, Double::class, defaultValue = "0.0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(mockData.toDouble(), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt empty data - Double`() = runTest {
        val mockData = ""
        datsStore.data
            .mapDecrypt<Double>(security, Double::class, defaultValue = "0.0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(0.0, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt - String`() = runTest {
        val mockData = "aaa"
        datsStore.data
            .mapDecrypt<String>(security, String::class, defaultValue = "") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(mockData, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt empty data - string`() = runTest {
        val mockData = ""
        datsStore.data
            .mapDecrypt<String>(security, String::class, defaultValue = "") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals("", awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt - Boolean`() = runTest {
        val mockData = "true"
        datsStore.data
            .mapDecrypt<Boolean>(security, Boolean::class, defaultValue = "false") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(mockData.toBoolean(), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt empty data - Boolean`() = runTest {
        val mockData = ""
        datsStore.data
            .mapDecrypt<Boolean>(security, Boolean::class, defaultValue = "false") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(false, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt - Float`() = runTest {
        val mockData = "89090"
        datsStore.data
            .mapDecrypt<Float>(security, Float::class, defaultValue = "0.0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(mockData.toFloat(), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt empty data - Float`() = runTest {
        val mockData = ""
        datsStore.data
            .mapDecrypt<Float>(security, Float::class, defaultValue = "0.0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(0f, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt - Long`() = runTest {
        val mockData = "89090"
        datsStore.data
            .mapDecrypt<Long>(security, Long::class, defaultValue = "0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(mockData.toLong(), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test mapDecrypt empty data - Long`() = runTest {
        val mockData = ""
        datsStore.data
            .mapDecrypt<Long>(security, Long::class, defaultValue = "0") {
                security.encryptData(mockData)
            }
            .test {
                Assertions.assertEquals(0L, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }
}
package tech.thdev.useful.encrypted.data.store.preferences.security

import android.os.Build
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.S])
@RunWith(RobolectricTestRunner::class)
internal class UsefulSecurityImplTest {

    private val security = UsefulSecurityImpl(useTest = true)

    @Test
    fun `test security`() {
        val originData = "origin-data"
        val encryptData = security.encryptData(originData)
        Assertions.assertEquals(3, encryptData.split(UsefulSecurityImpl.DELIMITER).size)
        val decryptData = security.decryptData(encryptData)
        Assertions.assertEquals(originData, decryptData)
    }
}
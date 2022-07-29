package tech.thdev.useful.encrypted.data.store.preferences.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import tech.thdev.useful.encrypted.data.store.preferences.security.util.aesGenerateKey
import tech.thdev.useful.encrypted.data.store.preferences.security.util.convertBase64
import tech.thdev.useful.encrypted.data.store.preferences.security.util.convertSecretKey
import tech.thdev.useful.encrypted.data.store.preferences.security.util.encode

internal class UsefulSecurityImpl : UsefulSecurity {

    @Synchronized
    override fun encryptData(data: String): String {
        val cipher = Cipher.getInstance(CIPHER_OPTION)
        val secretKey = aesGenerateKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val final = cipher.doFinal(data.toByteArray())

        return String.format("%s%s%s%s%s", secretKey.convertBase64(), DELIMITER, cipher.iv.encode(), DELIMITER, final.encode())
    }

    @Synchronized
    override fun decryptData(encryptData: String): String {
        val target = encryptData.split(DELIMITER)
        if (target.size != 3) {
            return ""
        }

        val secretKey = target[0].convertSecretKey()
        val encryptIv = Base64.decode(target[1], Base64.DEFAULT)
        val encryptTarget = Base64.decode(target[2], Base64.DEFAULT)

        val cipher = Cipher.getInstance(CIPHER_OPTION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(encryptIv))

        return String(cipher.doFinal(encryptTarget))
    }

    companion object {
        private const val CIPHER_OPTION = "AES/CBC/PKCS5PADDING"
        private const val DELIMITER = "|"
    }
}

fun generateUsefulSecurity(): UsefulSecurity =
    UsefulSecurityImpl()
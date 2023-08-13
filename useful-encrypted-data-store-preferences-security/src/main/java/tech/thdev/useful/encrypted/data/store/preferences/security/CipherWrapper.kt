package tech.thdev.useful.encrypted.data.store.preferences.security

import android.util.Base64
import androidx.annotation.VisibleForTesting
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import tech.thdev.useful.encrypted.data.store.preferences.security.util.aesGenerateKey
import tech.thdev.useful.encrypted.data.store.preferences.security.util.convertBase64
import tech.thdev.useful.encrypted.data.store.preferences.security.util.convertSecretKey
import tech.thdev.useful.encrypted.data.store.preferences.security.util.encode

internal class CipherWrapper(
    private val keyEntry: KeyStore.Entry?,
) {

    @Synchronized
    fun encryptData(data: String): String {
        return if (keyEntry != null) {
            if (data.length > RSA_MAX_LENGTH) {
                encrypt(data)
            } else {
                try {
                    encryptKeyStore(data)
                } catch (e: Exception) {
                    // RSA key limit
                    encrypt(data)
                }
            }
        } else {
            encrypt(data)
        }
    }

    private fun encrypt(data: String): String {
        val secretKey = aesGenerateKey()
        val cipher = Cipher.getInstance(CIPHER_OPTION).apply {
            init(Cipher.ENCRYPT_MODE, secretKey)
        }
        val final = cipher.doFinal(data.toByteArray())

        return String.format("%s%s%s%s%s", secretKey.convertBase64(), DELIMITER, cipher.iv.encode(), DELIMITER, final.encode())
    }

    private fun encryptKeyStore(data: String): String {
        val cipher = Cipher.getInstance(KEYSTORE_CIPHER_OPTION).apply {
            init(Cipher.ENCRYPT_MODE, (keyEntry as KeyStore.PrivateKeyEntry).certificate.publicKey)
        }
        val bytes = data.toByteArray(Charsets.UTF_8)
        val encryptedBytes = cipher.doFinal(bytes)

        return String.format("%s%s%s", encryptedBytes.encode(), DELIMITER, "end;")
    }

    fun decryptData(encryptData: String): String {
        if (encryptData.contains(DELIMITER).not()) {
            return encryptData
        }

        return if (keyEntry != null) {
            try {
                decrypt(encryptData)
            } catch (e: Exception) {
                try {
                    decryptKeyStore(encryptData)
                } catch (e: InternalSecureException) {
                    encryptData
                } catch (e: Exception) {
                    ""
                }
            }
        } else {
            try {
                decrypt(encryptData)
            } catch (e: InternalSecureException) {
                encryptData
            } catch (e: Exception) {
                ""
            }
        }
    }

    private fun decrypt(encryptData: String): String {
        val target = encryptData.split(DELIMITER)
        if (target.size != 3) {
            throw InternalSecureException("Not support")
        }

        val secretKey = target[0].convertSecretKey()
        val encryptIv = Base64.decode(target[1], Base64.DEFAULT)
        val encryptTarget = Base64.decode(target[2], Base64.DEFAULT)

        val cipher = Cipher.getInstance(CIPHER_OPTION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(encryptIv))

        return String(cipher.doFinal(encryptTarget))
    }

    private fun decryptKeyStore(encryptData: String): String {
        val target = encryptData.split(DELIMITER)
        if (target.size != 2) {
            throw InternalSecureException("Not support")
        }

        val cipher = Cipher.getInstance(KEYSTORE_CIPHER_OPTION).apply {
            init(Cipher.DECRYPT_MODE, (keyEntry as KeyStore.PrivateKeyEntry).privateKey)
        }

        val encryptedBytes = Base64.decode(target[0], Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return String(decryptedBytes)
    }

    companion object {

        private const val CIPHER_OPTION = "AES/CBC/PKCS5PADDING"

        @VisibleForTesting
        internal const val DELIMITER = "|"

        /**
         * AndroidKeystore
         */
        private const val KEYSTORE_CIPHER_OPTION = "RSA/ECB/PKCS1Padding"

        private const val RSA_MAX_LENGTH = 42
    }
}
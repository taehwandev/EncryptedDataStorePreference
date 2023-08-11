package tech.thdev.useful.encrypted.data.store.preferences.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.VisibleForTesting
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import tech.thdev.useful.encrypted.data.store.preferences.security.util.aesGenerateKey
import tech.thdev.useful.encrypted.data.store.preferences.security.util.convertBase64
import tech.thdev.useful.encrypted.data.store.preferences.security.util.convertSecretKey
import tech.thdev.useful.encrypted.data.store.preferences.security.util.encode


internal class UsefulSecurityImpl(
    private val keyStoreAlias: String = DEFAULT_KEY_STORE_ALIAS,
    useTest: Boolean = false,
) : UsefulSecurity {

    private var keyEntry: KeyStore.Entry? = null

    init {
        if (useTest.not()) {
            val keyStore = KeyStore.getInstance(PROVIDER_NAME).apply {
                load(null)
            }

            val supportSecure = if (keyStore.containsAlias(keyStoreAlias)) {
                true
            } else { // KeyStore one time register
                kotlin.runCatching {
                    KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA,
                        PROVIDER_NAME,
                    ).apply {
                        initialize(
                            KeyGenParameterSpec.Builder(keyStoreAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                                .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(KEY_LENGTH_BIT, RSAKeyGenParameterSpec.F4))
                                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                .setDigests(
                                    KeyProperties.DIGEST_SHA512,
                                    KeyProperties.DIGEST_SHA384,
                                    KeyProperties.DIGEST_SHA256
                                )
                                .setUserAuthenticationRequired(false)
                                .build()
                        )
                    }.generateKeyPair()
                    true
                }.getOrDefault(false)
            }

            if (supportSecure) {
                keyEntry = keyStore.getEntry(keyStoreAlias, null)
            }
        }
    }

    @Synchronized
    override fun encryptData(data: String): String {
        return if (keyEntry != null) {
            val cipher = Cipher.getInstance(KEYSTORE_CIPHER_OPTION).apply {
                init(Cipher.ENCRYPT_MODE, (keyEntry as KeyStore.PrivateKeyEntry).certificate.publicKey)
            }
            val bytes = data.toByteArray(Charsets.UTF_8)
            val encryptedBytes = cipher.doFinal(bytes)
            val base64EncryptedBytes = Base64.encode(encryptedBytes, Base64.DEFAULT)

            String(base64EncryptedBytes)
        } else {
            val secretKey = aesGenerateKey()
            val cipher = Cipher.getInstance(CIPHER_OPTION).apply {
                init(Cipher.ENCRYPT_MODE, secretKey)
            }
            val final = cipher.doFinal(data.toByteArray())

            String.format("%s%s%s%s%s", secretKey.convertBase64(), DELIMITER, cipher.iv.encode(), DELIMITER, final.encode())
        }
    }

    @Synchronized
    override fun decryptData(encryptData: String): String {
        return if (keyEntry != null) {
            try {
                decrypt(encryptData)
            } catch (e: Exception) {
                kotlin.runCatching {
                    decryptKeyStore(encryptData)
                }.onFailure {
                    it.printStackTrace()
                }.getOrDefault("")
            }
        } else {
            kotlin.runCatching {
                decrypt(encryptData)
            }.getOrDefault("")
        }
    }

    private fun decrypt(encryptData: String): String {
        val target = encryptData.split(DELIMITER)
        if (target.size != 3) {
            throw Exception("not support")
        }

        val secretKey = target[0].convertSecretKey()
        val encryptIv = Base64.decode(target[1], Base64.DEFAULT)
        val encryptTarget = Base64.decode(target[2], Base64.DEFAULT)

        val cipher = Cipher.getInstance(CIPHER_OPTION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(encryptIv))

        return String(cipher.doFinal(encryptTarget))
    }

    private fun decryptKeyStore(encryptData: String): String {
        val cipher = Cipher.getInstance(KEYSTORE_CIPHER_OPTION).apply {
            init(Cipher.DECRYPT_MODE, (keyEntry as KeyStore.PrivateKeyEntry).privateKey)
        }

        val base64EncryptedBytes = encryptData.toByteArray(Charsets.UTF_8)
        val encryptedBytes = Base64.decode(base64EncryptedBytes, Base64.DEFAULT)
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

        private const val PROVIDER_NAME = "AndroidKeyStore"

        private const val KEY_LENGTH_BIT = 2048

        internal const val DEFAULT_KEY_STORE_ALIAS = "tech.thdev.useful.encrypted.data.store.preferences"
    }
}

fun generateUsefulSecurity(
    keyStoreAlias: String = UsefulSecurityImpl.DEFAULT_KEY_STORE_ALIAS,
): UsefulSecurity =
    UsefulSecurityImpl(keyStoreAlias = keyStoreAlias)
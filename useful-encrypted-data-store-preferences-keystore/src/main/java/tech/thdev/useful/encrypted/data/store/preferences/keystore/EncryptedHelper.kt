package tech.thdev.useful.encrypted.data.store.preferences.keystore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

class EncryptedHelper(
    private val alias: String,
) {

    private val cipher by lazy {
        Cipher.getInstance("AES/GCM/NoPadding")
    }

    private val keyGenerator by lazy {
        KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            androidKeyStore,
        )
    }

    private val parameterSpec: KeyGenParameterSpec
        get() {
            return KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                build()
            }
        }

    fun encryptData(text: String): ByteArray {
        val key = keyGenerator.apply { init(parameterSpec) }.generateKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(text.toByteArray())
    }

    fun decryptData(encryptedData: ByteArray): String? {
        /**
         * Load the Android KeyStore instance using the
         * "AndroidKeyStore" provider to list out what entries are
         * currently stored.
         */
        val keyStore = KeyStore.getInstance(androidKeyStore).apply {
            load(null)
        }
        android.util.Log.d("TEMP", "keyStore.getEntry(alias, null) ${keyStore.getEntry(alias, null)}")
        return (keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.let { key ->
            cipher.init(Cipher.DECRYPT_MODE, key.secretKey, GCMParameterSpec(128, cipher.iv))
            cipher.doFinal(encryptedData).toString()
        }
    }

    companion object {
        private const val androidKeyStore = "AndroidKeyStore"
    }
}

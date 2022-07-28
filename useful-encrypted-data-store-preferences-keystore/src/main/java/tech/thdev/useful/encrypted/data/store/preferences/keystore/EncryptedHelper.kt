package tech.thdev.useful.encrypted.data.store.preferences.keystore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
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

    /**
     * Load the Android KeyStore instance using the
     * "AndroidKeyStore" provider to list out what entries are
     * currently stored.
     */
    private val keyStore by lazy {
        KeyStore.getInstance(androidKeyStore).apply {
            load(null)
        }
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

    private fun generateEncryptKey(): SecretKey =
        keyGenerator.also {
            it.init(parameterSpec)
        }.generateKey()

    fun encryptData(data: String): ByteArray {
        var temp = data
        while (temp.toByteArray().size % 16 != 0) {
            temp += "\u0020"
        }

        android.util.Log.d("TEMP", "encryptData $data, temp.toByteArray(Charsets.UTF_8) ${temp.toByteArray(Charsets.UTF_8)}")
        cipher.init(Cipher.ENCRYPT_MODE, generateEncryptKey())
        val final = cipher.doFinal(temp.toByteArray(Charsets.UTF_8))
        android.util.Log.d("TEMP", "encrypt final $final")
        return final
    }

    fun decryptData(encryptedData: ByteArray): String? {
        android.util.Log.i("TEMP", "decryptData $encryptedData")
        return (keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.let { key ->
            cipher.init(Cipher.DECRYPT_MODE, key.secretKey, GCMParameterSpec(128, cipher.iv))
            val value = cipher.doFinal(encryptedData).toString()
        android.util.Log.d("TEMP", "decrypt Data final $value")
        return value
        }
    }

    companion object {
        private const val androidKeyStore = "AndroidKeyStore"
    }
}

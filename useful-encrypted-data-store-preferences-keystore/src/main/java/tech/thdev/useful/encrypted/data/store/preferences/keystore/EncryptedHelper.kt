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

    private val keyGenerator by lazy {
        KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            androidKeyStore,
        )
    }

    private fun generateEncryptKey(): SecretKey =
        keyGenerator.also {
            it.init(
                KeyGenParameterSpec
                    .Builder(
                        alias,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        }.generateKey()

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

    @Synchronized
    fun encryptData(data: String): String {
        android.util.Log.d("TEMP", "encryptData $data, temp.toByteArray(Charsets.UTF_8) ${data.toByteArray(Charsets.UTF_8)}")
        val cipher = Cipher.getInstance(CIPHER_OPTION)
        cipher.init(Cipher.ENCRYPT_MODE, generateEncryptKey())
        val final = cipher.doFinal(data.toByteArray())
        android.util.Log.d("TEMP", "encrypt final $final")
        android.util.Log.d("TEMP", "encrypt final ${final.decodeToString()}")
        android.util.Log.i("TEMP", "cipher.iv size : ${cipher.iv.size} data :  ${cipher.iv.decodeToString()}")
        return cipher.iv.joinToString(",") + "|" + final.joinToString(",")
    }

    @Synchronized
    fun decryptData(encryptData: String): String {
        if (encryptData.isEmpty()) return ""

        val allEncrypted = encryptData.split("|")
        val encryptIv = allEncrypted.first().split(",").map { it.toByte() }.toByteArray()
        val encryptTarget = allEncrypted.last().split(",").map { it.toByte() }.toByteArray()

        android.util.Log.w("TEMP", "decryptData? $encryptTarget")
        android.util.Log.i("TEMP", "decryptData ${String(encryptTarget)}")
        val key = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
        android.util.Log.d("TEMP", "key? ${key.secretKey}")
        val cipher = Cipher.getInstance(CIPHER_OPTION)
        cipher.init(Cipher.DECRYPT_MODE, key.secretKey, GCMParameterSpec(128, encryptIv))
        val value = String(cipher.doFinal(encryptTarget))
        android.util.Log.d("TEMP", "decrypt Data final $value")
        return value
    }

    companion object {
        private const val androidKeyStore = "AndroidKeyStore"
        private const val CIPHER_OPTION = "AES/GCM/NoPadding"
    }
}
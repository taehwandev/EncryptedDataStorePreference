package tech.thdev.useful.encrypted.data.store.preferences.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.RSAKeyGenParameterSpec


internal class UsefulSecurityImpl(
    private val keyStoreAlias: String = DEFAULT_KEY_STORE_ALIAS,
    useTest: Boolean = false,
) : UsefulSecurity {

    private val keyStore = KeyStore.getInstance(PROVIDER_NAME).apply {
        load(null)
    }

    private val keyEntry: KeyStore.Entry? by lazy {
        if (useTest.not()) {
            val supportSecure = if (keyStore.containsAlias(keyStoreAlias)) {
                true
            } else { // KeyStore first time register
                kotlin.runCatching {
                    KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA,
                        PROVIDER_NAME,
                    ).apply {
                        initialize(
                            KeyGenParameterSpec.Builder(keyStoreAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                                .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(KEY_LENGTH_BIT, RSAKeyGenParameterSpec.F4))
                                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
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
                keyStore.getEntry(keyStoreAlias, null)
            } else {
                null
            }
        } else {
            null
        }
    }

    @Synchronized
    override fun encryptData(data: String): String =
        CipherWrapper(keyEntry).encryptData(data)

    @Synchronized
    override fun decryptData(encryptData: String): String =
        CipherWrapper(keyEntry).decryptData(encryptData)

    companion object {

        private const val PROVIDER_NAME = "AndroidKeyStore"

        private const val KEY_LENGTH_BIT = 2048

        internal const val DEFAULT_KEY_STORE_ALIAS = "tech.thdev.useful.encrypted.data.store.preferences"
    }
}

fun generateUsefulSecurity(
    keyStoreAlias: String = UsefulSecurityImpl.DEFAULT_KEY_STORE_ALIAS,
): UsefulSecurity =
    UsefulSecurityImpl(keyStoreAlias = keyStoreAlias)
package tech.thdev.useful.encrypted.data.store.preferences.security.util

import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

internal fun SecretKey.convertBase64(): String {
    val byteArray = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(byteArray)
    objectOutputStream.writeObject(this)
    return byteArray.toByteArray().encode()
}

internal fun String.convertSecretKey(): SecretKey {
    val objectInputStream = ObjectInputStream(ByteArrayInputStream(this.decode()))
    return objectInputStream.readObject() as SecretKey
}

/**
 * Base64 Default convert
 */
internal fun ByteArray.encode(): String =
    Base64.encodeToString(this, Base64.DEFAULT)

/**
 * String to ByteArray. Base64 Default
 */
internal fun String.decode(): ByteArray =
    Base64.decode(this, Base64.DEFAULT)

internal fun aesGenerateKey(): SecretKey =
    KeyGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_AES,
    ).also {
        it.init(256)
    }.generateKey()
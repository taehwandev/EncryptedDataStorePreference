package tech.thdev.useful.encrypted.data.store.preferences.security

interface UsefulSecurity {

    /**
     * Data to encryptData
     */
    fun encryptData(data: String): String

    /**
     * encryptData to decrypt
     */
    fun decryptData(encryptData: String): String
}
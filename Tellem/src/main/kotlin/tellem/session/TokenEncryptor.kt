package tellem.session

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object TokenEncryptor {
    //look for the way for storing secret keys on client
    private const val SECRET_KEY = "1234567890"
    private const val GCM_TAG_LENGTH = 128
    private const val IV_LENGTH = 12

    @Throws(Exception::class)
    fun encrypt(text: String): String? {
        val iv = ByteArray(IV_LENGTH)
        SecureRandom().nextBytes(iv)

        val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))

        val encrypted = cipher.doFinal(text.toByteArray())

        val combined = ByteArray(iv.size + encrypted.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)

        return Base64.getEncoder().encodeToString(combined)
    }

    @Throws(Exception::class)
    fun decrypt(text: String?): String {
        val decoded = Base64.getDecoder().decode(text)

        val iv = ByteArray(IV_LENGTH)
        val encrypted = ByteArray(decoded.size - IV_LENGTH)

        System.arraycopy(decoded, 0, iv, 0, IV_LENGTH)
        System.arraycopy(decoded, IV_LENGTH, encrypted, 0, encrypted.size)

        val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))

        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }
}
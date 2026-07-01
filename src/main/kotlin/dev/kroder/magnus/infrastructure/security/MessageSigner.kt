package dev.kroder.magnus.infrastructure.security

import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * HMAC-based message signer for authenticating Redis messages.
 * Uses HMAC-SHA256 for cryptographic integrity verification.
 *
 * Security Features:
 * - Message authentication (prevents tampering)
 * - Replay attack prevention (timestamp-based)
 * - Timing-safe comparison (prevents timing attacks)
 */
class MessageSigner(private val secret: String) {

    companion object {
        private const val SIGNED_MESSAGE_PARTS = 3
    }

    private val algorithm = "HmacSHA256"

    /**
     * Signs a message and returns the payload with embedded signature.
     * Format: "signature|timestamp|payload"
     *
     * @param payload The original message to sign
     * @return Signed message in format "signature|timestamp|payload"
     */
    fun sign(payload: String): String {
        val timestamp = System.currentTimeMillis()
        val dataToSign = "$timestamp|$payload"
        val signature = computeHmac(dataToSign)
        return "$signature|$dataToSign"
    }

    /**
     * Verifies a signed message and extracts the original payload.
     * Returns null if signature is invalid or message is too old.
     *
     * @param signedMessage The message to verify (format: "signature|timestamp|payload")
     * @param maxAgeMs Maximum age of the message in milliseconds (default 30s)
     * @return The original payload if valid, null otherwise
     */
    fun verify(signedMessage: String, maxAgeMs: Long = 30_000): String? {
        val parts = signedMessage.split("|", limit = SIGNED_MESSAGE_PARTS)
        if (parts.size != SIGNED_MESSAGE_PARTS) return null

        val (signature, timestampStr, payload) = parts

        // Parse timestamp
        val timestamp = timestampStr.toLongOrNull()

        var result: String? = null
        if (timestamp != null) {
            // Check message age (replay attack prevention)
            val messageAge = System.currentTimeMillis() - timestamp
            if (messageAge >= 0 && messageAge <= maxAgeMs) {
                // Verify HMAC signature
                val expectedSignature = computeHmac("$timestampStr|$payload")
                if (timingSafeEquals(signature, expectedSignature)) {
                    result = payload
                }
            }
        }

        return result
    }

    private fun computeHmac(data: String): String {
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(secret.toByteArray(Charsets.UTF_8), algorithm))
        return Base64.getEncoder().encodeToString(mac.doFinal(data.toByteArray(Charsets.UTF_8)))
    }

    /**
     * Timing-safe string comparison to prevent timing attacks.
     * Always compares all characters regardless of early mismatch.
     */
    private fun timingSafeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        var result = 0
        for (i in a.indices) {
            result = result or (a[i].code xor b[i].code)
        }
        return result == 0
    }
}

package com.example.marvelist.data.remote.networking

import com.example.marvelist.utils.toHexString
import java.security.DigestException
import java.security.MessageDigest

/**
 * Contains static API information and methods to construct requests.
 */
class MarvelApiInfo {
    companion object {
        // Base API construction.
        const val baseEndpoint = "https://gateway.marvel.com/"
        const val publicKey = "c8a433817be427ab58915765a8dfeaa2"
        // Default query parameter values.
        const val DEFAULT_RESULT_LIMIT = 5
        const val DEFAULT_FORMAT_TYPE = "comic" // Results should show a series or comics
        const val DEFAULT_FORMAT = "comic"       // Results should show digital comics or physical.
        const val DEFAULT_ORDER_BY = "-focDate" // Sort by date modified in descending order.
        // Misc.
        const val hashType = "MD5"
        private const val privateKey = "1bb2861c82d637c7d73a2c1a48c1b15bf487d969"

        /**
         * Calculates the MD5 hash that's required in all requests.
         * Takes the (timeStamp + privateKey + publicKey)
         *
         * @param timeStamp the time stamp which will be used in the hash calculation.
         * @param md the MessageDigest object used to generate the MD5 hash.
         *
         * @return A hash object that will calculate
         */
        fun getHashValueString(timeStamp: String, md: MessageDigest): String {
            try {
                val digestContent = timeStamp + privateKey + publicKey
                md.update(digestContent.toByteArray())
                return md.digest().toHexString()
            } catch (csne: CloneNotSupportedException) {
                throw DigestException("Couldn't make the digest")
            }
        }
    }
}
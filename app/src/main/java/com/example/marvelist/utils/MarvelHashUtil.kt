package com.example.marvelist.utils

import com.example.marvelist.data.remote.networking.MarvelApiInfo
import java.security.MessageDigest
import javax.inject.Inject

/**
 * Class dedicated to calculating the hash value for API requests.
 *
 * Calculation : MD5(timeStamp + privateKey + publicKey)
 */
class MarvelHashUtil @Inject constructor(private val md: MessageDigest) {
    fun calculateHash(timeStamp: String) =
        MarvelApiInfo.getHashValueString(timeStamp, md)
}
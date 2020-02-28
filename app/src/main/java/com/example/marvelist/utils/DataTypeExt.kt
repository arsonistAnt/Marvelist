package com.example.marvelist.utils

import com.example.marvelist.data.local.ComicDetails
import com.example.marvelist.data.remote.models.MarvelJson

/**
 * Convert the byte array into hex string values.
 */
fun ByteArray.toHexString(): String {
    val hexStringBuilder = StringBuilder()
    for (byte in this) {
        hexStringBuilder.append(String.format("%02x", byte))
    }
    return hexStringBuilder.toString()
}

/**
 * Convert [MarvelJson.Comic] to a [ComicDetails] object.
 */
fun MarvelJson.Comic.toComicDetails(): ComicDetails {
    return ComicDetails(this)
}
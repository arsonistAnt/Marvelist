package com.example.marvelist.utils


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
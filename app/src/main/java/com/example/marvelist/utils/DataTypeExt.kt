package com.example.marvelist.utils

import com.example.marvelist.data.local.ComicDetails
import com.example.marvelist.data.remote.models.MarvelJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
fun MarvelJson.Comic.toComicDetails() = ComicDetails(this)

/**
 * Define extension function that provides a setup for async processing and main thread observing.
 */
fun <T> Observable<T>.scheduleAsync(): Observable<T> = this.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
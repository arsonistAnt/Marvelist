package com.example.marvelist.utils

import android.annotation.SuppressLint
import android.widget.TextView
import com.example.marvelist.data.database.ComicInfo
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.local.ComicDetail.Companion.getReadingProgressEnum
import com.example.marvelist.data.remote.models.MarvelJson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat

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
 * Convert [MarvelJson.Comic] to a [ComicDetail] object.
 *
 * @see [ComicDetail]
 */
fun MarvelJson.Comic.toComicDetails() = ComicDetail(this)

@SuppressLint("SimpleDateFormat")
fun MarvelJson.Date.formatDateString(): String {
    // The date from the api is iso 8601 so this pattern is used to capture it.
    val iso8601FormattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date)
    iso8601FormattedDate?.let {
        // Format the iso 8601 date into a 'MMM d, yyyy' format.
        return SimpleDateFormat("MMM d, yyyy").format(iso8601FormattedDate)
    }

    return "No date available."
}

/**
 * Define extension function that provides a setup for async processing and main thread observing.
 */
fun <T> Observable<T>.scheduleAsync(): Observable<T> = this.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

/**
 * Define extension function that provides a setup for async processing and main thread observing.
 */
fun Completable.scheduleAsync(): Completable = this.subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

/**
 * Convert the [ComicDetail] object into a [ComicInfo] entity object.
 *
 * @see [ComicInfo]
 */
fun ComicDetail.toComicInfo() = this.run {
    ComicInfo(id, digitalId, title, comicDescription, format, progress.ordinal, thumbnailUrl)
}

/**
 * Convert the [ComicInfo] entity object into a [ComicDetail] object.
 *
 * @see [ComicDetail]
 */
fun ComicInfo.toComicDetails() = this.run {
    val readingEnumVal = getReadingProgressEnum(readingProgress)
    ComicDetail(
        readingEnumVal,
        comicId,
        comicDigitalId,
        comicTitle,
        description,
        comicThumbnailPath,
        -1,
        comicFormat
    )
}

/**
 * Calculates the maximum number of lines in the text view and assign it.
 */
fun TextView.calculateMaxLines() {
    viewTreeObserver.addOnGlobalLayoutListener {
        val numOfMaxLines: Int = height / lineHeight
        maxLines = numOfMaxLines
    }
}
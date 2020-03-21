package com.example.marvelist.utils

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.marvelist.data.database.ComicInfo
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.local.ComicDetail.Companion.getReadingProgressEnum
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.models.ResponseJson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat

// Other Extensions

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

// Network Model Extensions

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

// Local Model Extensions

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

// RX Java Extensions

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
 * Handles any network errors and get the [ResponseJson.Container] object from the [ResponseJson.Wrapper]
 * if the network response is successful.
 *
 * @param networkHandler the [NetworkResponseHandler] object that will create the appropriate [NetworkResponseHandler.Response] object.
 * @param networkResponse the a mutable live data of type [NetworkResponseHandler.Response] that will be used to observe the network state.
 */
fun Observable<ResponseJson.Wrapper>.handleNetworkResponse(
    networkHandler: NetworkResponseHandler,
    networkResponse: MutableLiveData<NetworkResponseHandler.Response>
): Observable<ResponseJson.Container> = this.scheduleAsync()
    .scheduleAsync()
    .doOnError { err ->
        Timber.e(err)
        val response = NetworkResponseHandler.Response(
            err.message ?: "",
            -1,
            NetworkResponseHandler.Status.FAILED
        )
        networkResponse.value = response
    }
    .doOnNext { responseJson ->
        networkResponse.value = networkHandler.createNetworkResult(responseJson.code)
    } // Get the payload from the wrapper if network response is successful.
    .map { it.data }

// Android View Extensions

/**
 * Calculates the maximum number of lines in the text view and assign it.
 */
fun TextView.calculateMaxLines() {
    viewTreeObserver.addOnGlobalLayoutListener {
        val numOfMaxLines: Int = height / lineHeight
        maxLines = numOfMaxLines
    }
}
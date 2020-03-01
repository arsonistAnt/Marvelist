package com.example.marvelist.data.remote.networking.comicpaging

import androidx.paging.PositionalDataSource
import com.example.marvelist.data.local.ComicDetails
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
import com.example.marvelist.utils.scheduleAsync
import com.example.marvelist.utils.toComicDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

/**
 * DataSource class that will emit comic API results in chunks.
 *
 * @see MarvelComicService
 */
class ComicDataSource @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) :
    PositionalDataSource<ComicDetails>() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ComicDetails>) {
        val hash = marvelHashUtil.calculateHash("${params.startPosition}")
        marvelService.getComicListPayload(
            "${params.startPosition}",
            params.startPosition,
            hash,
            params.loadSize
        )
            .scheduleAsync()
            .doOnError {
                // TODO: Add error callback here.
                Timber.e(it)
            }
            .doOnNext { responseJson ->
                // TODO: Add error callback here.
                Timber.i(responseJson.status)
            }
            .map {
                it.data
            }
            .subscribe { container ->
                val results = container.results.map { it.toComicDetails() }
                callback.onResult(results)
            }.addTo(disposable)
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<ComicDetails>
    ) {
        val hash = marvelHashUtil.calculateHash("0")
        marvelService.getComicListPayload(
            "0",
            params.requestedStartPosition,
            hash,
            params.requestedLoadSize
        )
            .scheduleAsync()
            .doOnError {
                // TODO: Add error callback here.
                Timber.e(it)
            }
            .doOnNext { responseJson ->
                // TODO: Add error callback here.
                Timber.i(responseJson.status)
            }
            .map {
                it.data
            }
            .subscribe { container ->
                val results = container.results.map { it.toComicDetails() }
                callback.onResult(results, params.requestedStartPosition, params.requestedLoadSize)
            }.addTo(disposable)
    }

    override fun invalidate() {
        super.invalidate()
        // Dispose any RxJava Observables that may be executing.
        disposable.dispose()
    }
}
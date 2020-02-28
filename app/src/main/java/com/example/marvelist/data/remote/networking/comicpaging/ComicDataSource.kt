package com.example.marvelist.data.remote.networking.comicpaging

import androidx.paging.PositionalDataSource
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
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
    PositionalDataSource<MarvelJson.Comic>() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MarvelJson.Comic>) {
        val hash = marvelHashUtil.calculateHash("0")
        marvelService.getComicListPayload(
            "0",
            params.startPosition,
            hash,
            params.loadSize
        )
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
                val results = container.results
                callback.onResult(results)
            }.addTo(disposable)
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<MarvelJson.Comic>
    ) {
        val hash = marvelHashUtil.calculateHash("0")
        marvelService.getComicListPayload(
            "0",
            params.requestedStartPosition,
            hash,
            params.requestedLoadSize
        )
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
                val results = container.results
                callback.onResult(results, params.requestedStartPosition + 5, container.count)
            }.addTo(disposable)
    }

    override fun invalidate() {
        super.invalidate()
        // Dispose any RxJava Observables that may be executing.
        disposable.dispose()
    }
}
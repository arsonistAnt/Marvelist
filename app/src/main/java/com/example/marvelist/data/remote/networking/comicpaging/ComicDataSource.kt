package com.example.marvelist.data.remote.networking.comicpaging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.remote.models.ResponseJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
import com.example.marvelist.utils.NetworkResponseHandler
import com.example.marvelist.utils.handleNetworkResponse
import com.example.marvelist.utils.toComicDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

/**
 * DataSource class that will emit comic API results in chunks.
 *
 * @see MarvelComicService
 */
class ComicDataSource @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil,
    private val networkResponseHandler: NetworkResponseHandler
) :
    PositionalDataSource<ComicDetail>(), NetworkResponseHandler.ResponseCallback {

    // Observe in the View layer to keep track of network state.
    override val networkResponse = MutableLiveData<NetworkResponseHandler.Response>()

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ComicDetail>) {
        val hash = marvelHashUtil.calculateHash("${params.startPosition}")
        marvelService.getComicListPayload(
            "${params.startPosition}",
            params.startPosition,
            hash,
            params.loadSize
        )
            .handleNetworkResponse(networkResponseHandler, networkResponse)
            .subscribe(
                // Successful response.
                { container: ResponseJson.Container ->
                    val results = container.results.map { it.toComicDetails() }
                    callback.onResult(results)
                },
                // Error occurred submit an empty list.
                {
                    callback.onResult(emptyList())
                }
            ).addTo(disposable)
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<ComicDetail>
    ) {
        val hash = marvelHashUtil.calculateHash("0")
        marvelService.getComicListPayload(
            "0",
            params.requestedStartPosition,
            hash,
            params.requestedLoadSize
        )
            .handleNetworkResponse(networkResponseHandler, networkResponse)
            .subscribe(
                // On success.
                { container: ResponseJson.Container ->
                    val results = container.results.map { it.toComicDetails() }
                    callback.onResult(results, params.requestedStartPosition, container.total)
                },
                // On error occurred, submit an empty list to stop paging for data.
                {
                    callback.onResult(emptyList(), 0)
                }
            ).addTo(disposable)
    }

    override fun invalidate() {
        super.invalidate()
        // Dispose any RxJava Observables that may be executing.
        disposable.dispose()
    }
}
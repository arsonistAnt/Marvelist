package com.example.marvelist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.repository.ComicRepository
import com.example.marvelist.utils.NetworkResponseHandler
import com.example.marvelist.utils.handleNetworkResponse
import com.example.marvelist.utils.toComicDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class ComicDetailsViewModel @Inject constructor(
    private val comicRepo: ComicRepository,
    private val networkResponseHandler: NetworkResponseHandler
) :
    ViewModel() {
    // Disposable object to rid of any lingering RxJava calls.
    private val disposables = CompositeDisposable()
    // LiveData to observe changes to the comic details data.
    private val _comicDetailData = MutableLiveData<ComicDetail>()
    val comicDetailData: LiveData<ComicDetail>
        get() = _comicDetailData
    // LiveData that contains the network state.
    private val _comicDetailsNetworkStatus = MutableLiveData<NetworkResponseHandler.Response>()
    val comicDetailsNetworkStatus: LiveData<NetworkResponseHandler.Response>
        get() = _comicDetailsNetworkStatus

    /**
     * Obtain comic book details by specified ID and update the [_comicDetailData] live data.
     *
     * @param comicId the comic book id.
     */
    fun getComicById(comicId: Int) {
        comicRepo.getComicDetailsApi(comicId)
            .handleNetworkResponse(networkResponseHandler, _comicDetailsNetworkStatus)
            .map { data ->
                data.results.firstOrNull()
            }
            .subscribe(
                // On success
                { comicJson: MarvelJson.Comic? ->
                    comicJson?.let {
                        _comicDetailData.value = it.toComicDetails()
                    }
                },
                // On Error
                { err ->
                    Timber.e(err)
                }
            ).addTo(disposables)
    }

    /**
     * Cleanup any lingering resources.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
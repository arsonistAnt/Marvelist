package com.example.marvelist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelist.data.local.ComicDetails
import com.example.marvelist.data.repository.ComicRepository
import com.example.marvelist.utils.scheduleAsync
import com.example.marvelist.utils.toComicDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class ComicDetailsViewModel @Inject constructor(private val comicRepo: ComicRepository) :
    ViewModel() {
    // Disposable object to rid of any lingering RxJava calls.
    private val disposables = CompositeDisposable()
    // LiveData to observe changes to the comic details data.
    private val _comicDetailData = MutableLiveData<ComicDetails>()
    val comicDetailData: LiveData<ComicDetails>
        get() = _comicDetailData

    /**
     * Obtain comic book details by specified ID and update the [_comicDetailData] live data.
     *
     * @param comicId the comic book id.
     */
    fun getComicById(comicId: Int) {
        comicRepo.getComic(comicId).scheduleAsync()
            .doOnError {
                // TODO: Error checking needs to be done here.
                Timber.e(it)
            }
            .doOnNext {
                // TODO: Check for correct status response.
                Timber.i(it.status)
            }
            .map { wrapper ->
                wrapper.data.results.firstOrNull()
            }
            .subscribe { comicJson ->
                comicJson?.let {
                    _comicDetailData.value = it.toComicDetails()
                }
            }.addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
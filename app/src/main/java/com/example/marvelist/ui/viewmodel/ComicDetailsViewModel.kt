package com.example.marvelist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.repository.ComicRepository
import com.example.marvelist.utils.toComicDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ComicDetailsViewModel @Inject constructor(private val comicRepo: ComicRepository) :
    ViewModel() {
    // Disposable object to rid of any lingering RxJava calls.
    private val disposables = CompositeDisposable()
    // LiveData to observe changes to the comic details data.
    private val _comicDetailData = MutableLiveData<ComicDetail>()
    val comicDetailData: LiveData<ComicDetail>
        get() = _comicDetailData

    /**
     * Obtain comic book details by specified ID and update the [_comicDetailData] live data.
     *
     * @param comicId the comic book id.
     */
    fun getComicById(comicId: Int) {
        comicRepo.getComicDetailsApi(comicId)
            .map { wrapper ->
                wrapper.data.results.firstOrNull()
            }
            .subscribe { comicJson ->
                comicJson?.let {
                    _comicDetailData.value = it.toComicDetails()
                }
            }.addTo(disposables)
    }

    /**
     * Cleanup any lingering resources.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
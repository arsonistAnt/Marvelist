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

/**
 * A [ViewModel] that exposes a list of [ComicDetail] data from the local database. This model
 * is also in charge updating and removal of the data in the database.
 */
class ReadingListViewModel @Inject constructor(private val comicRepo: ComicRepository) :
    ViewModel() {
    private val _savedComics = MutableLiveData<List<ComicDetail>>()
    val savedComics: LiveData<List<ComicDetail>>
        get() = _savedComics

    // Disposable object to rid of any lingering RxJava calls.
    private val disposables = CompositeDisposable()

    /**
     * Retrieve all [ComicDetail] data from the local database. This call will
     * update the [_savedComics] live data once the retrieval is complete.
     */
    fun getAllSavedComics() {
        comicRepo.getAllComics()
            .subscribe { comicInfoList ->
                _savedComics.value = comicInfoList.map { it.toComicDetails() }
            }.addTo(disposables)
    }

    /**
     * Cleanup any lingering resources.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
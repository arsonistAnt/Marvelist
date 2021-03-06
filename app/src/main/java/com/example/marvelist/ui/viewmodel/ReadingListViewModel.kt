package com.example.marvelist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelist.data.database.ComicInfo
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
     * Remove a specific comic from the local database.
     *
     * @param comicId the comic id of the comic to remove.
     */
    fun removeComic(comicId: Int) {
        comicRepo.deleteComicById(comicId).subscribe()
    }

    /**
     * Remove a list of comics from the local database.
     *
     * @param comics a list of [ComicInfo] objects to remove from the database.
     */
    fun removeComics(comics: List<ComicInfo>) {
        comicRepo.removeComics(comics).subscribe()
    }

    /**
     * Update the reading status of the comic in the database.
     *
     * @param comic the [ComicInfo] object that will have its reading progress updated.
     */
    fun updateComicReadingStatus(comic: ComicDetail, readingProgress: Int) {
        comicRepo.updateComic(comic.id, readingProgress).subscribe()
    }

    /**
     * Cleanup any lingering resources.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
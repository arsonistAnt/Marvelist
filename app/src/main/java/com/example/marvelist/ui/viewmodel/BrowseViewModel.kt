package com.example.marvelist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.local.ComicPreview
import com.example.marvelist.data.repository.ComicRepository
import com.example.marvelist.utils.toComicInfo
import javax.inject.Inject

/**
 * A [ViewModel] that exposes and handles paging for Comic data in the API. This model
 * typically exposes data geared towards "front page" viewing and casual browsing.
 */
class BrowseViewModel @Inject constructor(private val comicRepo: ComicRepository) :
    ViewModel() {

    private val comicPagedListConfig: PagedList.Config =
        PagedList.Config.Builder().setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .setInitialLoadSizeHint(10)
            .setPageSize(10).build()

    val comicPagedList: LiveData<PagedList<ComicPreview>>

    init {
        // Initialize live data for the comic pager.
        val comicDataSourceFactory =
            comicRepo.getComicDataSourceFactory().map { it as ComicPreview }
        comicPagedList = LivePagedListBuilder(comicDataSourceFactory, comicPagedListConfig).build()
    }

    /**
     * Store the [ComicDetail] into the local database.
     *
     * @param comic the object to save into the local database.
     */
    fun saveComicLocalDatabase(comic: ComicDetail) {
        comicRepo.insertComic(comic.toComicInfo()).subscribe()
    }
}
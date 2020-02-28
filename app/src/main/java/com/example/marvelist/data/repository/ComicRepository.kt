package com.example.marvelist.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.data.remote.networking.comicpaging.ComicDataSourceFactory
import com.example.marvelist.utils.MarvelHashUtil
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) {
    private val pagedListConfig: PagedList.Config =
        PagedList.Config.Builder().setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .setInitialLoadSizeHint(10)
            .setPageSize(10).build()

    /**
     * Return a PagedList for [MarvelJson.Comic] wrapped in a LiveData format.
     */
    fun getComicPagedLiveData(): LiveData<PagedList<MarvelJson.Comic>> {
        val comicDataSourceFactory = ComicDataSourceFactory(marvelService, marvelHashUtil)
        return LivePagedListBuilder(comicDataSourceFactory, pagedListConfig).build()
    }
}
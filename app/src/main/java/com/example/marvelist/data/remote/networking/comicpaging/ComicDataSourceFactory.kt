package com.example.marvelist.data.remote.networking.comicpaging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
import com.example.marvelist.utils.NetworkResponseHandler
import javax.inject.Inject

/**
 * Factory in charge of creating the ComicDataSource object.
 */
class ComicDataSourceFactory @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil,
    private val networkResponseHandler: NetworkResponseHandler
) : DataSource.Factory<Int, ComicDetail>() {

    val currDataSource = MutableLiveData<ComicDataSource>()

    override fun create(): DataSource<Int, ComicDetail> {
        val newDataSource = ComicDataSource(marvelService, marvelHashUtil, networkResponseHandler)
        currDataSource.postValue(newDataSource)
        return newDataSource
    }
}
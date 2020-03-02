package com.example.marvelist.data.remote.networking.comicpaging

import androidx.paging.DataSource
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
import javax.inject.Inject

/**
 * Factory in charge of creating the ComicDataSource object.
 */
class ComicDataSourceFactory @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) : DataSource.Factory<Int, ComicDetail>() {

    override fun create(): DataSource<Int, ComicDetail> {
        return ComicDataSource(marvelService, marvelHashUtil)
    }
}
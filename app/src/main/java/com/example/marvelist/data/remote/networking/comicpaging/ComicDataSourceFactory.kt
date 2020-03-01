package com.example.marvelist.data.remote.networking.comicpaging

import androidx.paging.DataSource
import com.example.marvelist.data.local.ComicDetails
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
import javax.inject.Inject

class ComicDataSourceFactory @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) : DataSource.Factory<Int, ComicDetails>() {

    override fun create(): DataSource<Int, ComicDetails> {
        return ComicDataSource(marvelService, marvelHashUtil)
    }
}
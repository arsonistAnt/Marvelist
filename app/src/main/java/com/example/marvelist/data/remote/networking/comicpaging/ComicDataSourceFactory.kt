package com.example.marvelist.data.remote.networking.comicpaging

import androidx.paging.DataSource
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.utils.MarvelHashUtil
import javax.inject.Inject

class ComicDataSourceFactory @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) : DataSource.Factory<Int, MarvelJson.Comic>() {

    override fun create(): DataSource<Int, MarvelJson.Comic> {
        val source = ComicDataSource(marvelService, marvelHashUtil)
        return source
    }
}
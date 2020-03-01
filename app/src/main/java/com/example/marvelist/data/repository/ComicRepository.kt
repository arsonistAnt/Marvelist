package com.example.marvelist.data.repository

import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.data.remote.networking.comicpaging.ComicDataSourceFactory
import com.example.marvelist.utils.MarvelHashUtil
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) {


    /**
     * Returns a DataSourceFactory for the [MarvelJson.Comic] network data.
     */
    fun getComicDataSourceFactory(): ComicDataSourceFactory =
        ComicDataSourceFactory(marvelService, marvelHashUtil)
}
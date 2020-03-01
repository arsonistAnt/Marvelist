package com.example.marvelist.data.repository

import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.models.ResponseJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.data.remote.networking.comicpaging.ComicDataSourceFactory
import com.example.marvelist.utils.MarvelHashUtil
import io.reactivex.Observable
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil
) {


    /**
     * Construct a DataSourceFactory for the [MarvelJson.Comic] network data.
     *
     * @return a [ComicDataSourceFactory] object.
     */
    fun getComicDataSourceFactory(): ComicDataSourceFactory =
        ComicDataSourceFactory(marvelService, marvelHashUtil)

    /**
     * Request comic book details specified by comic ID, using the
     * [marvelService] Retrofit service object.
     *
     * @param comicId the comic book id.
     *
     * @return a observable of type ResponseJson.Wrapper which contains the API results.
     */
    fun getComic(comicId: Int): Observable<ResponseJson.Wrapper> {
        val timeStamp = "$comicId"
        val hash = marvelHashUtil.calculateHash(timeStamp)
        return marvelService.getComic(comicId, timeStamp, hash)
    }
}
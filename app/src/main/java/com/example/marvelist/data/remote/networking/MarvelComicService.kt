package com.example.marvelist.data.remote.networking

import com.example.marvelist.data.remote.models.ResponseJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MarvelComicService {

    /**
     * Retrieve the result which contains a list of comic Json objects.
     *
     * @param formatType determines whether to show comic issues or a collection of comics (E.g. "comic" | "collection")
     * @param timeStamp the current time stamp which is used to compute the hash required for the API request.
     * @param apiKey the public key for the API.
     * @param resultLimit the amount of comic Json's to retrieve from the result.
     * @param resultOffset the amount to skip over the results returned from the API result.
     *
     * @return an Observable of type [ResponseJson.Wrapper]
     */
    @GET("v1/public/comics")
    fun getComicListPayload(
        @Query("ts")
        timeStamp: String,
        @Query("offset")
        resultOffset: Int,
        @Query("hash")
        hash: String,
        @Query("limit")
        resultLimit: Int = MarvelApiInfo.DEFAULT_RESULT_LIMIT,
        @Query("orderBy")
        sortBy: String = MarvelApiInfo.DEFAULT_ORDER_BY,
        @Query("format")
        format: String = MarvelApiInfo.DEFAULT_FORMAT,
        @Query("formatType")
        formatType: String = MarvelApiInfo.DEFAULT_FORMAT_TYPE,
        @Query("apikey")
        apiKey: String = MarvelApiInfo.publicKey
    ): Observable<ResponseJson.Wrapper>

    /**
     * Request Comic book details specified by ID.
     *
     * @param comicId the comic book id to request details for.
     *
     * @return an Observable of type [ResponseJson.Wrapper]
     */
    @GET("v1/public/comics/{id}")
    fun getComic(
        @Path("id")
        comicId: Int,
        @Query("ts")
        timeStamp: String,
        @Query("hash")
        hash: String,
        @Query("apikey")
        apiKey: String = MarvelApiInfo.publicKey
    ): Observable<ResponseJson.Wrapper>
}
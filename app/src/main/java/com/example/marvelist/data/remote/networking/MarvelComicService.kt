package com.example.marvelist.data.remote.networking

import com.example.marvelist.data.remote.models.ResponseJson
import io.reactivex.Observable
import retrofit2.http.GET
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
        @Query("formatType")
        formatType: String = MarvelApiInfo.DEFAULT_FORMAT_TYPE,
        @Query("apikey")
        apiKey: String = MarvelApiInfo.publicKey
    ): Observable<ResponseJson.Wrapper>
}
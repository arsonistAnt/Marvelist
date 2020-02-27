package com.example.marvelist.data.remote.models

import com.squareup.moshi.JsonClass

/**
 * Every successful call will return a [Wrapper] object, which contains metadata about the call
 * and a [Container] object, which displays pagination information and an array of the results
 * returned by this call. This pattern is consistent even if you are requesting a single object.
 *
 * @see [Marvel Documentation](https://developer.marvel.com/documentation/apiresults)
 */
interface ResponseJson {
    /**
     * The base parent, a payload deserialized into this class.
     * Contains status code and description of the status.
     *
     * @see Container
     */
    @JsonClass(generateAdapter = true)
    data class Wrapper(
        val code: Int,
        val status: String,
        val data: Container
    )

    /**
     * Contains the results of the API calls and pagination info.
     *
     * @see Wrapper
     */
    @JsonClass(generateAdapter = true)
    data class Container(
        val offset: Int,
        val limit: Int,
        val total: Int,
        val count: Int,
        val results: List<MarvelJson.Comic>
    )


}


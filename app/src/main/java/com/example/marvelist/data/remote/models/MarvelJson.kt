package com.example.marvelist.data.remote.models

import com.squareup.moshi.JsonClass

/**
 * An interface that contains Json classes for deserializing information pertaining to the Marvel API.
 */
interface MarvelJson {
    /**
     * Stores general comic information (unique id, title, description, etc).
     *
     * This Json object contains nested Json classes in its properties:
     * @see Thumbnail
     * @see RelatedIssues
     * @see
     */
    @JsonClass(generateAdapter = true)
    data class Comic(
        val id: Int,
        val digitalId: Int,
        val title: String,
        val description: String?,
        val issueNumber: Int,
        val thumbnail: Thumbnail,
        val collectedIssues: List<RelatedIssues>?,
        val creators: CreatorGroup?,
        val dates : List<Date>
    )

    /**
     * Stores information about the thumbnail cover for the comic, more
     * specifically for the [Comic] class.
     */
    data class Thumbnail(
        val path: String,
        val extension: String
    )

    /**
     * Stores an endpoint to comic issues that relate to the [Comic] class.
     */
    data class RelatedIssues(
        val resourceURI: String,
        val name: String
    )

    /**
     * Stores information about the creators of the comic.
     *
     * @see
     */
    data class CreatorGroup(
        val available : Int,
        val items : List<Creator>?
    )

    /**
     * Information about the creator themselves.
     */
    data class Creator(
        val name : String,
        val role : String
    )

    /**
     * Stores relevant dates pertaining to this issue.
     */
    data class Date(
        val type : String,
        val date : String
    )
}






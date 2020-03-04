package com.example.marvelist.data.local

import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.utils.ThumbnailVariant

/**
 * A class that stores comic information passed through either the primary constructor or
 * the secondary constructor which takes a [MarvelJson.Comic] object. This class will mostly
 * used as an in-memory representational model for the Android View classes.
 * (E.g. showing the information on RecyclerView's or TextView's)
 */
open class ComicDetail : ComicPreview {
    var collection: List<MarvelJson.RelatedIssues>? = null
    var author: MarvelJson.CreatorGroup? = null
    var dates: List<MarvelJson.Date>? = null
    var progress: ReadingProgress = ReadingProgress.UNREAD

    companion object {
        fun constructThumbnailUrl(thumbnail: MarvelJson.Thumbnail) =
            "${thumbnail.path}${ThumbnailVariant.portraitLarge}${thumbnail.extension}"

        fun getReadingProgressEnum(enumVal: Int) = when (enumVal) {
            0 -> ReadingProgress.IN_PROGRESS
            1 -> ReadingProgress.READ
            else -> ReadingProgress.UNREAD
        }
    }

    /**
     * Primary constructor to store comic book information.
     */
    constructor(
        progress: ReadingProgress,
        id: Int,
        digitalId: Int,
        title: String,
        description: String,
        thumbnailUrl: String,
        issueNumber: Int,
        format: String
    ) : super(id, digitalId, title, description, thumbnailUrl, issueNumber, format) {
        this.progress = progress
    }

    /**
     * Constructor that takes a [MarvelJson.Comic] object and transfers
     * the information to this class.
     */
    constructor (marvelComicJson: MarvelJson.Comic) : super(
        marvelComicJson.id,
        marvelComicJson.digitalId,
        marvelComicJson.title,
        marvelComicJson.description ?: "",
        marvelComicJson.thumbnail.path,
        marvelComicJson.issueNumber,
        marvelComicJson.format
    ) {
        collection = marvelComicJson.collectedIssues
        author = marvelComicJson.creators
        dates = marvelComicJson.dates
    }

    /**
     * Return the string representation of the [ReadingProgress] enum class.
     *
     * @param readingProgress the enum class object.
     */
    fun readingStatus(): String = when (progress) {
        ReadingProgress.READ -> "Read"
        ReadingProgress.IN_PROGRESS -> "In Progress"
        else -> "Unread"
    }
}

/**
 * Enum values that help track the current reading progress in
 * a [ComicDetail] class.
 */
enum class ReadingProgress(progressInt: Int) {
    IN_PROGRESS(0), READ(1), UNREAD(2)
}
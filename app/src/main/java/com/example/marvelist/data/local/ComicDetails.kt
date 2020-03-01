package com.example.marvelist.data.local

import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.utils.ThumbnailVariant

/**
 * The in-memory representation of the Comic json object, will mostly be used by Android's View classes.
 * Assign [MarvelJson.Comic] data to [ComicDetails] class properties.
 */
class ComicDetails
    (marvelComicJson: MarvelJson.Comic) : ComicPreview(
    marvelComicJson.id,
    marvelComicJson.digitalId,
    marvelComicJson.title,
    marvelComicJson.description ?: "",
    constructThumbnailUrl(marvelComicJson.thumbnail),
    marvelComicJson.issueNumber
) {
    var collection: List<MarvelJson.RelatedIssues>? = marvelComicJson.collectedIssues
    var author: MarvelJson.CreatorGroup? = marvelComicJson.creators
    var dates: List<MarvelJson.Date>? = marvelComicJson.dates

    companion object {
        fun constructThumbnailUrl(thumbnail: MarvelJson.Thumbnail) =
            "${thumbnail.path}${ThumbnailVariant.portraitLarge}${thumbnail.extension}"
    }


}
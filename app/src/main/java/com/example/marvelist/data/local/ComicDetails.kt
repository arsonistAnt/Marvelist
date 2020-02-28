package com.example.marvelist.data.local

import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.utils.ThumbnailVariant

/**
 * The in-memory representation of the Comic json object, will mostly be used by Android's View classes.
 */
class ComicDetails : ComicBase {
    override var title: String
    override var comicDescription: String
    override var thumbnailUrl: String
    override var issueNo: Int

    var collection: List<MarvelJson.RelatedIssues>?
    var author: MarvelJson.CreatorGroup?
    var dates: List<MarvelJson.Date>?

    constructor() {
        title = ""
        comicDescription = ""
        thumbnailUrl = ""
        issueNo = -1
        collection = null
        author = null
        dates = null
    }

    /**
     *  Assign [MarvelJson.Comic] data to [ComicDetails] class properties.
     *
     *  @param marvelComicJson the Json object to transfer data from.
     */
    constructor(marvelComicJson: MarvelJson.Comic) {
        title = marvelComicJson.title
        comicDescription = marvelComicJson.description ?: ""
        // TODO: Choose thumbnail variant based off of pixel density type of device.
        thumbnailUrl =
            "${marvelComicJson.thumbnail.path}${ThumbnailVariant.portraitLarge}${marvelComicJson.thumbnail.extension}"
        issueNo = marvelComicJson.issueNumber
        collection = marvelComicJson.collectedIssues
        author = marvelComicJson.creators
        dates = marvelComicJson.dates
    }
}
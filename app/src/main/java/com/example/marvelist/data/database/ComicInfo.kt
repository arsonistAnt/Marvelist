package com.example.marvelist.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.marvelist.data.local.ComicDetail

/**
 * Database entity version of [ComicDetail] class.
 */
@Entity(
    tableName = "comic_info",
    indices = [
        Index(value = ["comicId", "description"])]
)
class ComicInfo(
    @PrimaryKey
    val comicId: Int,
    val comicDigitalId: Int,
    val comicTitle: String,
    val description: String,
    val comicFormat: String,
    var readingProgress: Int,
    var comicThumbnailPath: String
)

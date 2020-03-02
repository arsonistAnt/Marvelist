package com.example.marvelist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.marvelist.data.local.ComicDetail

/**
 * Database entity version of [ComicDetail] class.
 */
@Entity(tableName = "comic_info")
class ComicInfo(
    @PrimaryKey
    val comicId: Int,
    val comicDigitalId: Int,
    val comicTitle: String,
    val description: String,
    val comicFormat: String,
    val readingProgress: Int,
    val comicThumbnailPath: String
)

package com.example.marvelist.data.database

import androidx.room.*
import io.reactivex.Observable

/**
 * DAO class that contains method pertaining to [ComicInfo] objects.
 */
@Dao
interface ComicInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comicInfo: ComicInfo)

    @Query("SELECT * from comic_info")
    fun getAllComicInfo(): Observable<List<ComicInfo>>

    @Query("DELETE FROM comic_info WHERE comicId = :comicId")
    fun deleteComicByID(comicId: Int)

    @Delete
    fun deleteComics(comics: List<ComicInfo>)
}

/**
 * A [RoomDatabase] that contains DAO objects relating to comics.
 */
@Database(entities = [ComicInfo::class], version = 1)
abstract class ComicDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicInfoDao
}
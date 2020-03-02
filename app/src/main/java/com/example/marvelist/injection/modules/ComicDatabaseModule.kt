package com.example.marvelist.injection.modules

import android.content.Context
import androidx.room.Room
import com.example.marvelist.data.database.ComicDatabase
import com.example.marvelist.data.database.ComicInfoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module class that constructs and provides a [ComicDatabase]
 * by the [Context] given.
 */
@Module
class ComicDatabaseModule {
    @Singleton
    @Provides
    fun comicDatabase(context: Context): ComicDatabase {
        return Room.databaseBuilder(
            context,
            ComicDatabase::class.java,
            "comic-database"
        ).build()
    }

    @Singleton
    @Provides
    fun comicInfoDao(comicDB: ComicDatabase): ComicInfoDao {
        return comicDB.comicDao()
    }
}
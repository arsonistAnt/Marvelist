package com.example.marvelist.data.repository

import com.example.marvelist.data.database.ComicInfo
import com.example.marvelist.data.database.ComicInfoDao
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.remote.models.ResponseJson
import com.example.marvelist.data.remote.networking.MarvelComicService
import com.example.marvelist.data.remote.networking.comicpaging.ComicDataSourceFactory
import com.example.marvelist.utils.MarvelHashUtil
import com.example.marvelist.utils.scheduleAsync
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

/**
 * Class that retrieves/saves comic book information from the local
 * storage or from a network request.
 */
class ComicRepository @Inject constructor(
    private val marvelService: MarvelComicService,
    private val marvelHashUtil: MarvelHashUtil,
    private val comicInfoDao: ComicInfoDao
) {

    // Methods that deal with network requests.

    /**
     * Construct a DataSourceFactory for the [MarvelJson.Comic] network data.
     *
     * @return a [ComicDataSourceFactory] object.
     */
    fun getComicDataSourceFactory(): ComicDataSourceFactory =
        ComicDataSourceFactory(marvelService, marvelHashUtil)

    /**
     * Request comic book details from API specified by comic ID, using the
     * [marvelService] Retrofit service object.
     *
     * @param comicId the comic book id.
     *
     * @return an [Observable] of type [ResponseJson.Wrapper] which contains the API results.
     */
    fun getComicDetailsApi(comicId: Int): Observable<ResponseJson.Wrapper> {
        val timeStamp = "$comicId"
        val hash = marvelHashUtil.calculateHash(timeStamp)
        return marvelService.getComic(comicId, timeStamp, hash)
            .scheduleAsync()
            .doOnError {
                // TODO: Error checking needs to be done here.
                Timber.e(it)
            }
            .doOnNext {
                // TODO: Check for correct status response.
                Timber.i(it.status)
            }
    }

    // Methods that deal with local storage or Room.

    /**
     * Insert the [ComicInfo] into the local Room database.
     *
     * @param comic the object to save into the Room database.
     *
     * @return a [Completable] type observable.
     */
    fun insertComic(comic: ComicInfo): Completable =
        Completable.fromAction { comicInfoDao.insert(comic) }
            .scheduleAsync()
            .doOnError {
                Timber.e(it)
            }
            .doOnComplete {
                Timber.i("Finished database operation.")
            }

    /**
     * Retrieve all [ComicInfo] from the local Room database.
     *
     * @return an [Observable] of type List<ComicInfo>.
     */
    fun getAllComics(): Observable<List<ComicInfo>> = comicInfoDao.getAllComicInfo()
        .scheduleAsync()
        .doOnError {
            Timber.e(it)
        }

    /**
     * Remove a comic from the local database.
     *
     * @param comicId the comic id to query for deletion.
     *
     * @return a [Completable] type observable.
     */
    fun deleteComicById(comicId: Int): Completable =
        Completable.fromAction { comicInfoDao.deleteComicByID(comicId) }
            .scheduleAsync()
            .doOnError {
                Timber.e(it)
            }
            .doOnComplete {
                Timber.i("Finished database operation.")
            }


    /**
     * Remove a list of comics from the local database.
     *
     * @param comics a list of [ComicInfo] objects to remove from the database.
     *
     * @return a [Completable] type observable.
     */
    fun removeComics(comics: List<ComicInfo>): Completable =
        Completable.fromAction { comicInfoDao.deleteComics(comics) }
            .scheduleAsync()
            .doOnError {
                Timber.e(it)
            }
            .doOnComplete {
                Timber.i("Finished database operation.")
            }

    /**
     * Update the comic's data from the local database.
     *
     * @param comicId the comic id of the comic entity to update.
     * @param readingProgress the new reading progress.
     *
     * @return a [Completable] type observable.
     */
    fun updateComic(comicId: Int, readingProgress: Int): Completable =
        Completable.fromAction { comicInfoDao.updateComicInfo(comicId, readingProgress) }
            .scheduleAsync()
            .doOnError {
                Timber.e(it)
            }
            .doOnComplete {
                Timber.i("Finished database operation.")
            }
}
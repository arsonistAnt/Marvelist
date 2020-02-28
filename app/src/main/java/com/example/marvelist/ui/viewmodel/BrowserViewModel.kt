package com.example.marvelist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.data.repository.ComicRepository
import javax.inject.Inject

/**
 * A [ViewModel] that exposes and handles paging for Comic data in the API.
 */
class BrowserViewModel @Inject constructor(private val comicRepository: ComicRepository) :
    ViewModel() {

    val comicPagedList: LiveData<PagedList<MarvelJson.Comic>> =
        comicRepository.getComicPagedLiveData()
}
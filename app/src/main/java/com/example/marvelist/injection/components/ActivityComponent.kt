package com.example.marvelist.injection.components

import com.example.marvelist.injection.modules.ComicDatabaseModule
import com.example.marvelist.injection.modules.ContextModule
import com.example.marvelist.injection.modules.MarvelServiceModule
import com.example.marvelist.injection.modules.MessageDigestModule
import com.example.marvelist.ui.viewmodel.BrowseViewModel
import com.example.marvelist.ui.viewmodel.ComicDetailsViewModel
import com.example.marvelist.ui.viewmodel.ReadingListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * This component will used as the base dagger component that will provide all dependencies.
 */
@Singleton
@Component(
    modules = [MarvelServiceModule::class,
        MessageDigestModule::class,
        ContextModule::class,
        ComicDatabaseModule::class]
)
interface ActivityComponent {
    val browseViewModel: BrowseViewModel
    val comicDetailsViewModel: ComicDetailsViewModel
    val readingListViewModel: ReadingListViewModel
}
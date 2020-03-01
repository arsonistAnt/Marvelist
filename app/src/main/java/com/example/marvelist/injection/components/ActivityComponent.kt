package com.example.marvelist.injection.components

import com.example.marvelist.injection.modules.MarvelServiceModule
import com.example.marvelist.injection.modules.MessageDigestModule
import com.example.marvelist.ui.viewmodel.BrowseViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * This component will used as the base dagger component that will provide all dependencies.
 */
@Singleton
@Component(
    modules = [MarvelServiceModule::class,
        MessageDigestModule::class]
)
interface ActivityComponent {
    val browseViewModel: BrowseViewModel
}
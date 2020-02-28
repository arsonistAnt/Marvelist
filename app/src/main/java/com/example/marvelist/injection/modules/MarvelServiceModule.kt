package com.example.marvelist.injection.modules

import com.example.marvelist.data.remote.networking.MarvelApiInfo
import com.example.marvelist.data.remote.networking.MarvelComicService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Module that provides a [MarvelComicService] type retrofit service.
 */
@Module
class MarvelServiceModule {
    @Singleton
    @Provides
    fun service(): MarvelComicService =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(MarvelApiInfo.baseEndpoint)
            .build()
            .create(MarvelComicService::class.java)
}
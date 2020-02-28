package com.example.marvelist.injection.modules

import com.example.marvelist.data.remote.networking.MarvelApiInfo
import dagger.Module
import dagger.Provides
import java.security.MessageDigest

/**
 * Module that constructs & provides a [MessageDigest] according to the hash type of the [MarvelApiInfo] class.
 */
@Module
class MessageDigestModule {
    @Provides
    fun getMessageDigest(): MessageDigest {
        return MessageDigest.getInstance(MarvelApiInfo.hashType)
    }
}
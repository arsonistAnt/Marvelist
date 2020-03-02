package com.example.marvelist.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Module that provides a [Context] object.
 */
@Module
class ContextModule(private val context: Context) {
    @Provides
    fun getMessageDigest() = context
}
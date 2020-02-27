package com.example.marvelist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvelist.R
import com.example.marvelist.data.remote.MarvelComicService
import com.example.marvelist.data.remote.MarvelApiInfo
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.security.MessageDigest

class NavigationHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val test = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(MarvelApiInfo.baseEndpoint)
            .build()
            .create(MarvelComicService::class.java)

        val md = MessageDigest.getInstance(MarvelApiInfo.hashType)

        test.getComicListPayload("1234", 0, MarvelApiInfo.getHashValueString("1234", md))
            .subscribeOn(Schedulers.io())
            .doOnError {
                Timber.e(it)
            }
            .subscribe {
                print(it)
            }



    }
}

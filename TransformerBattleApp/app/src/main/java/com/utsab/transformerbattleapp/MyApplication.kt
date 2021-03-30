package com.utsab.transformerbattleapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.utsab.transformerbattleapp.helpers.ApiInterface
import com.utsab.transformerbattleapp.helpers.Constants
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApplication: Application() {
    private var appInstance: MyApplication? = null

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}
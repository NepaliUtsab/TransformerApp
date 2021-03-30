package com.utsab.transformerbattleapp.helpers.di

import com.utsab.transformerbattleapp.helpers.ApiInterface
import com.utsab.transformerbattleapp.helpers.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val baseUrl = Constants.BASE_URL

    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor;
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor {
                    var originalRequest = it.request()
                    val request = originalRequest.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(originalRequest.method(), originalRequest.body())
                            .build()
                    it.proceed(request)
                }.build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): ApiInterface {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface::class.java)
    }

}
package com.example.newgonggong.Presentation.di

import com.example.newgonggong.BuildConfig
import com.example.newgonggong.BuildConfig.*
import com.example.newgonggong.data.api.APIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.*

fun provideAPIService(retrofit : Retrofit) : APIService{
    return retrofit.create(APIService::class.java)
}

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideGsonConverterFactory() : GsonConverterFactory{
    return GsonConverterFactory.create()
}

fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if(BuildConfig.DEBUG){
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }else{
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, SECONDS)
        .addInterceptor(interceptor)
        .build()
}
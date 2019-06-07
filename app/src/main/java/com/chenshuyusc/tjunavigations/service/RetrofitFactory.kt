package com.chenshuyusc.tjunavigations.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络请求工厂
 */
object RetrofitFactory {

    private val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(false)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://restapi.amap.com/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val api: RetrofitService = retrofit.create(RetrofitService::class.java)
}
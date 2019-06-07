package com.chenshuyusc.tjunavigations.service

import com.chenshuyusc.tjunavigations.entity.Distance
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 网络请求接口
 */
interface RetrofitService {
    @GET("v3/direction/walking")
    fun getDistance(@Query("key")key:String,@Query("origin") origin: String, @Query("destination") destination: String): Deferred<Distance>
}
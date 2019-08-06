package com.josephuszhou.base.network.request

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
interface ApiService {

    @GET
    fun get(@Url url: String, @QueryMap maps: LinkedHashMap<String, Any>): Call<ResponseBody>

    @Streaming
    @GET
    fun download(@Url url: String): Call<ResponseBody>
}
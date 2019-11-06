package com.josephuszhou.base.network

import com.josephuszhou.base.network.request.DownloadRequest
import com.josephuszhou.base.network.request.GetRequest
import com.josephuszhou.base.network.util.NetUtil
import com.josephuszhou.insdownload.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author senfeng.zhou
 * @date 2019-07-10
 * @desc
 */
class HttpClient private constructor() {

    companion object {

        private const val DEFAULT_TIMEOUT = 10L
//        private const val DEFAULT_RETRY_TIMES = 3
//        private const val DEFAULT_RETRY_DELAY = 500

        @Volatile
        private var singleton: HttpClient? = null

        fun getInstance(): HttpClient {
            if (singleton == null) {
                synchronized(HttpClient::class.java) {
                    if (singleton == null) {
                        singleton = HttpClient()
                    }
                }
            }
            return singleton as HttpClient
        }

    }

    private var okHttpClientBuilder = OkHttpClient.Builder()
    private var glideOkHttpClientBuilder = OkHttpClient.Builder()
    private var retrofitBuilder = Retrofit.Builder()

    init {
        okHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.hostnameVerifier(NetUtil.DefaultHostnameVerifier())

        glideOkHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        glideOkHttpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        glideOkHttpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        glideOkHttpClientBuilder.hostnameVerifier(NetUtil.DefaultHostnameVerifier())

        val sslParams = NetUtil.getSSLSocketFactory(null, null, null)
        sslParams.sSLSocketFactory?.let {
            sslParams.trustManager?.let { it1 ->
                okHttpClientBuilder.sslSocketFactory(it, it1)
            }
        }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addNetworkInterceptor(logging)
        }
    }

    fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        return okHttpClientBuilder
    }

    fun getOKHttpClient(): OkHttpClient {
        return okHttpClientBuilder.build()
    }

    fun getRetrofitBuilder(): Retrofit.Builder {
        return retrofitBuilder
    }

    fun get(url: String): GetRequest {
        return GetRequest(url)
    }

    fun downLoad(url: String): DownloadRequest {
        return DownloadRequest(url)
    }

}
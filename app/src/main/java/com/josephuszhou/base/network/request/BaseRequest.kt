package com.josephuszhou.base.network.request

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.josephuszhou.base.network.HttpClient
import com.josephuszhou.base.network.entity.ApiResult
import com.josephuszhou.base.network.entity.HttpParams
import com.josephuszhou.base.network.proxy.CallClazzProxy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import java.lang.reflect.Modifier
import java.lang.reflect.Type


/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
abstract class BaseRequest<R : BaseRequest<R>>(protected var url: String) {

    private lateinit var baseUrl: String
    protected var httpParams: HttpParams
    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient
    protected lateinit var apiService: ApiService
    private var gson: Gson

    init {
        httpParams = HttpParams()
        gson = GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .serializeNulls()
            .create()
    }

    fun baseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    fun params(httpParams: HttpParams) {
        this.httpParams = httpParams
    }

    /**
     * 预留接口，可以根据需求自定义 OkHttpClient
     */
    private fun generateOkHttpClient(): OkHttpClient.Builder {
        return HttpClient.getInstance().getOkHttpClientBuilder()
    }

    /**
     * 预留接口，可以根据需求自定义 Retrofit
     */
    open fun generateRetrofit(): Retrofit.Builder {
        val retrofitBuilder = HttpClient.getInstance().getRetrofitBuilder()
        return retrofitBuilder.baseUrl(baseUrl)
    }

    protected fun build() {
        val okHttpClientBuilder = generateOkHttpClient()
        val retrofitBuilder = generateRetrofit()
        okHttpClient = okHttpClientBuilder.build()
        retrofit = retrofitBuilder.client(okHttpClient).build()
        apiService = retrofit.create(ApiService::class.java)
    }

    protected abstract fun generateRequest(): Call<ResponseBody>

    /**
     * 处理请求结果
     */
    suspend fun <T> execute(type: Type): ApiResult<T>  = withContext(Dispatchers.IO) {
        // 通过代理解析拿到实时运行时的真实 ApiResult 类型
        val proxy = object : CallClazzProxy<ApiResult<T>, T>(type) {}
        val proxyType = proxy.getIType()

        val response = this@BaseRequest.apply {
            build()
        }.generateRequest().execute()
        var apiResult = ApiResult<T>()

        if (!response.isSuccessful) {
            apiResult.code = response.code()
            apiResult.msg = response.message()
        } else {
            val body = response.body()
            body?.let {
                val bodyString = body.string()
                try {
                    apiResult = gson.fromJson(bodyString, proxyType)
                } catch (e: Exception) {
                    e.printStackTrace()
                    apiResult.msg = e.message
                } finally {
                    body.close()
                }
            }
        }
        apiResult
    }

    /**
     * 示例代码调用
     */
    /*val httpParams = HttpParams("packtype", 4)
    httpParams.put("lang", "cn")
    val result = HttpClient.getInstance().get("bbq.php")
        .baseUrl("https://a.happy.com")
        .params(httpParams)
        .execute<TestEntity>(TestEntity::class.java)

    val result1 = HttpClient.getInstance().get("1.json")
        .baseUrl("https://download.out.t")
        .execute<List<TestEntity>>(object : TypeToken<List<TestEntity>>() {}.type)*/

}
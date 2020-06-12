package com.josephuszhou.base.network.request

import okhttp3.ResponseBody
import retrofit2.Call
import com.josephuszhou.base.network.entity.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
class GetRequest(url: String) : BaseRequest<GetRequest>(url) {

    override fun generateRequest(): Call<ResponseBody> {
        return apiService.get(url, httpParams.urlParamsMap)
    }

    suspend fun executeHtml(): ApiResult<String> = withContext(Dispatchers.IO) {
        val apiResult = ApiResult<String>()
        try {
            val response = this@GetRequest.apply{
                build()
            }.generateRequest().execute()

            if (!response.isSuccessful && response.code() != 200) {
                apiResult.code = response.code()
                apiResult.msg = response.message()

                if (response.code() == 302) {
                    apiResult.msg = response.headers()["location"]
                }
            } else {
                apiResult.code = 1
                val body = response.body()
                if (body == null) {
                    apiResult.data = ""
                } else {
                    apiResult.data = body.string()
                }
                body?.close()
            }
        } catch (e: Exception) {
            apiResult.code = -1
            apiResult.msg = e.message
        }

        apiResult
    }

}
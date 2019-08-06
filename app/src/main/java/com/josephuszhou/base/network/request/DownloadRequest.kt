package com.josephuszhou.base.network.request

import android.content.Context
import android.media.MediaScannerConnection
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import com.josephuszhou.base.network.HttpClient
import com.josephuszhou.base.network.entity.ApiResult
import com.josephuszhou.base.util.MediaUtil
import java.io.FileOutputStream

/**
 * @author senfeng.zhou
 * @date 2019-08-05
 * @desc
 */
class DownloadRequest(url: String) : BaseRequest<DownloadRequest>(url) {

    override fun generateRetrofit(): Retrofit.Builder {
        return HttpClient.getInstance().getRetrofitBuilder()
    }

    override fun generateRequest(): Call<ResponseBody> {
        return apiService!!.download(url)
    }

    fun executeDownload(context: Context, name: String, isVideo: Boolean): ApiResult<String> {
        val apiResult = ApiResult<String>()
        try {
            val response = build().generateRequest().execute()

            if (!response.isSuccessful) {
                apiResult.code = response.code()
                apiResult.msg = response.message()
            } else {
                apiResult.code = 1
                val body = response.body()
                val file = if (isVideo) {
                    MediaUtil.buildVideoFile(name, url)
                } else {
                    MediaUtil.buildImageFile(name, url)
                }
                val fos = FileOutputStream(file)
                val inputStream = body!!.byteStream()
                val byteTemp = ByteArray(1024 * 4)
                while (true) {
                    val read = inputStream.read(byteTemp)
                    if (read == -1) break
                    fos.write(byteTemp, 0, read)
                }
                fos.flush()
                fos.close()
                apiResult.data = file.absolutePath
                MediaScannerConnection.scanFile(context.applicationContext, arrayOf(file.absolutePath),
                    null, null)
            }
        } catch (e: Exception) {
            apiResult.code = -1
            apiResult.msg = e.message
        }

        return apiResult
    }

}
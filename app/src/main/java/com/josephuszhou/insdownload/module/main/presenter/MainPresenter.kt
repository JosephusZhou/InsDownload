package com.josephuszhou.insdownload.module.main.presenter

import android.text.TextUtils
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.josephuszhou.base.network.HttpClient
import com.josephuszhou.insdownload.module.main.activity.MainActivity
import com.josephuszhou.insdownload.module.main.entity.InsEntity
import java.util.regex.Pattern

/**
 * @author senfeng.zhou
 * @date 2019-07-27
 * @desc MainActivity 对应逻辑和数据处理
 */
class MainPresenter(private val mainScope: CoroutineScope,
                    private val mainActivity: MainActivity
) {

    // 解析 ins 帖子
    fun requestInsPost(insUrl: String) = mainScope.launch(Dispatchers.Main) {
        mainActivity.showLoadingDialog()
        val urlPath = insUrl.replace("https://www.instagram.com/", "")
        val result = HttpClient.getInstance().get(urlPath).apply {
            baseUrl("https://www.instagram.com/")
        }.executeHtml()
        if (result.success) {
            mainActivity.hideLoadingDialog()
            result.data?.let {
                val insList = parseInsHtml(it)
                mainActivity.onImageListData(insList)
            } ?: mainActivity.onImageListData(ArrayList<InsEntity>())
        } else {
            mainActivity.hideLoadingDialog()
            Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
        }
    }

    // 解析 html
    private suspend fun parseInsHtml(html: String): ArrayList<InsEntity> = withContext(Dispatchers.Default) {
        val insList = ArrayList<InsEntity>()
        val jsonReg = "<script type=\"text/javascript\">window\\._sharedData = (.*?);</script>"
        val pattern = Pattern.compile(jsonReg)
        val matcher = pattern.matcher(html)
        while (matcher.find()) {
            val json: String = matcher.group(1)
            try {
                val jsonObject = JSONObject(json)
                val media = jsonObject.getJSONObject("entry_data")
                    .getJSONArray("PostPage")
                    .getJSONObject(0)
                    .getJSONObject("graphql")
                    .getJSONObject("shortcode_media")

                val postCode = media.getString("shortcode")

                if (media.has("is_video") && media.getBoolean("is_video")) {
                    // 视频
                    val insEntity = InsEntity(true)
                    insEntity.videoUrl = media.getString("video_url")
                    // 获取视频封面
                    val insEntityTemp = parseImageData(media.getJSONArray("display_resources"))
                    insEntityTemp.imageUrl?.let {
                        insEntity.thumbnailUrl = insEntityTemp.thumbnailUrl
                        insEntity.name = postCode
                        insList.add(insEntity)
                    }
                } else {
                    // 图片
                    if (media.has("edge_sidecar_to_children")) {
                        val edges = media.getJSONObject("edge_sidecar_to_children")
                            .getJSONArray("edges")
                        for(i in 0 until edges.length()) {
                            val insEntity =
                                parseImageData(edges.getJSONObject(i).getJSONObject("node").getJSONArray("display_resources"))
                            insEntity.imageUrl?.let {
                                insEntity.name = String.format("%s_%s", postCode, i.toString())
                                insList.add(insEntity)
                            }
                        }
                    } else {
                        val insEntity = parseImageData(media.getJSONArray("display_resources"))
                        insEntity.imageUrl?.let {
                            insEntity.name = postCode
                            insList.add(insEntity)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        insList
    }

    // 解析单张图片的数据
    private fun parseImageData(jsonArray: JSONArray): InsEntity {
        val insEntity = InsEntity(false)
        var minWidth = 9999999
        var maxWidth = -1
        try {
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (minWidth > obj.getInt("config_width")) {
                    minWidth = obj.getInt("config_width")
                    insEntity.thumbnailUrl = obj.getString("src")
                }
                if (maxWidth < obj.getInt("config_width")) {
                    maxWidth = obj.getInt("config_width")
                    insEntity.imageUrl = obj.getString("src")
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return insEntity
    }

    // 下载
    fun requestDownload(url: String, name: String?, isVideo: Boolean, position: Int) = mainScope.launch(Dispatchers.Main) {
        var savedName: String
        if (TextUtils.isEmpty(name)) {
            savedName = System.currentTimeMillis().toString()
        } else {
            savedName = name!!
        }
        val result = HttpClient.getInstance().downLoad(url)
            .executeDownload(mainActivity, savedName, isVideo)
        if (result.success) {
            result.data?.let {
                mainActivity.onDownloadData(it, position)
            } ?: mainActivity.onDownloadError(position)
        } else {
            Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
            mainActivity.onDownloadError(position)
        }
    }

}
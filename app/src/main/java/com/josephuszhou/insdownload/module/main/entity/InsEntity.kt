package com.josephuszhou.insdownload.module.main.entity

/**
 * @author senfeng.zhou
 * @date 2019-07-24
 * @desc
 */
class InsEntity(var isVideo: Boolean) {

    var name:String? = null

    var thumbnailUrl: String? = null
    var imageUrl: String? = null
    var videoUrl: String? = null

    var localPath: String? = null
    var downloadStatus: Int = 0
}
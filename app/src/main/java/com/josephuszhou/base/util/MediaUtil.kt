package com.josephuszhou.base.util

import android.os.Environment
import java.io.File

/**
 * @author senfeng.zhou
 * @date 2019-08-05
 * @desc 多媒体处理
 */
class MediaUtil {

    companion object {

        private const val IMAGE_RELATIVE_PATH: String = "Pictures/ins"
        private const val VIDEO_RELATIVE_PATH: String = "Movies/ins"

        private fun getImageMimeType(url: String): String {
            return if (url.contains(".jpg")) {
                "image/jpeg"
            } else {
                "image/*"
            }
        }

        private fun getImageSuffix(url: String): String {
            return if (url.contains(".jpg")) {
                ".jpg"
            } else {
                ".jpg"
            }
        }

        fun buildImageFile(name: String, url: String): File {
            // before Q, we create a new file to put the image file to a subPath for Pictures
            val file = File(Environment.getExternalStorageDirectory(), IMAGE_RELATIVE_PATH)
            if (!file.exists()) {
                file.mkdir()
            }
            return File(file, name + getImageSuffix(url))
        }

        /*fun buildImageUri(context: Context, name: String, url: String): Uri? {
            // After Q, we insert uri to put the image file to a subPath for Pictures
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DESCRIPTION, "ins image")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DISPLAY_NAME, name)
            values.put(MediaStore.Images.Media.MIME_TYPE, getImageMimeType(url))
            values.put(MediaStore.Images.Media.TITLE, name)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, IMAGE_RELATIVE_PATH)
            return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }*/

        private fun getVideoMimeType(url: String): String {
            return if (url.contains(".mp4")) {
                "video/mp4"
            } else {
                "video/*"
            }
        }

        private fun getVideoSuffix(url: String): String {
            return if (url.contains(".mp4")) {
                ".mp4"
            } else {
                ".mp4"
            }
        }

        fun buildVideoFile(name: String, url: String): File {
            // before Q, we create a new file to put the video file to a subPath for Movies
            val file = File(Environment.getExternalStorageDirectory(), VIDEO_RELATIVE_PATH)
            if (!file.exists()) {
                file.mkdir()
            }
            return File(file, name + getVideoSuffix(url))
        }

        /*fun buildVideoUri(context: Context, name: String, url: String): Uri? {
            // After Q, we insert uri to put the video file to a subPath for Movies
            val values = ContentValues()
            values.put(MediaStore.Video.Media.DESCRIPTION, "ins video")
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis())
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Video.Media.DATE_MODIFIED, System.currentTimeMillis())
            values.put(MediaStore.Video.Media.DISPLAY_NAME, name)
            values.put(MediaStore.Video.Media.MIME_TYPE, getVideoMimeType(url))
            values.put(MediaStore.Video.Media.TITLE, name)
            values.put(MediaStore.Video.Media.RELATIVE_PATH, VIDEO_RELATIVE_PATH)
            return context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        }*/
    }

}
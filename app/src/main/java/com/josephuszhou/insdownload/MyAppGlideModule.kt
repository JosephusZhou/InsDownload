package com.josephuszhou.insdownload

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * @author senfeng.zhou
 * @date 2019-07-09
 * @desc
 */
@GlideModule
class MyAppGlideModule: AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
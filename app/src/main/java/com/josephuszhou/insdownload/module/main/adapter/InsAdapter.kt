package com.josephuszhou.insdownload.module.main.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.josephuszhou.insdownload.GlideApp
import com.josephuszhou.insdownload.R
import com.josephuszhou.insdownload.module.main.entity.InsEntity

/**
 * @author senfeng.zhou
 * @date 2019-07-24
 * @desc Ins 帖子图片列表适配器
 */
class InsAdapter : BaseQuickAdapter<InsEntity, BaseViewHolder>(R.layout.item_ins) {

    override fun convert(helper: BaseViewHolder, item: InsEntity?) {
        item?.let {
            GlideApp.with(mContext)
                .load(it.thumbnailUrl)
                .placeholder(R.mipmap.ic_placeholder)
                .into(helper.getView(R.id.iv_preview))
            // video 类型加上标志
            if (it.isVideo) {
                helper.setVisible(R.id.iv_play, true)
            } else {
                helper.setVisible(R.id.iv_play, false)
            }
            if (TextUtils.isEmpty(it.localPath)) {
                helper.setText(R.id.tv_path, "")
                helper.setVisible(R.id.tv_path, false)
                helper.setEnabled(R.id.btn_download, true)
                if(it.downloadStatus == 0) {
                    helper.setVisible(R.id.layout_loading, false)
                } else {
                    helper.setVisible(R.id.layout_loading, true)
                }
            } else {
                helper.setVisible(R.id.layout_loading, false)
                helper.setText(R.id.tv_path, it.localPath)
                helper.setVisible(R.id.tv_path, true)
                helper.setEnabled(R.id.btn_download, false)
            }
        }
        helper.addOnClickListener(R.id.btn_download)
    }

}
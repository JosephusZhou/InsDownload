package com.josephuszhou.insdownload.module.about

import android.content.Context
import android.content.Intent
import com.josephuszhou.base.activity.BaseBindingActivity
import com.josephuszhou.insdownload.R
import com.josephuszhou.insdownload.databinding.ActivityAboutBinding


class AboutActivity : BaseBindingActivity<ActivityAboutBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_about

    override fun init() {
        setTitle(R.string.about)
    }

    override fun goBack() = true

}

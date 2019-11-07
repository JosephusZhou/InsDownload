package com.josephuszhou.insdownload.module.help

import android.content.Context
import android.content.Intent
import com.josephuszhou.base.activity.BaseBindingActivity
import com.josephuszhou.insdownload.R
import com.josephuszhou.insdownload.databinding.ActivityHelpBinding

class HelpActivity : BaseBindingActivity<ActivityHelpBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, HelpActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_help

    override fun init() {
        setTitle(R.string.help)
    }

    override fun goBack() = true

}

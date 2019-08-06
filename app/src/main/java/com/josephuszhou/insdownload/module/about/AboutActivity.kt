package com.josephuszhou.insdownload.module.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.josephuszhou.base.activity.BaseActivity
import com.josephuszhou.insdownload.BuildConfig
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.josephuszhou.insdownload.R.layout.activity_about)

        setTitle(com.josephuszhou.insdownload.R.string.about)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        tv_version.text = String.format("v%s", BuildConfig.VERSION_NAME)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

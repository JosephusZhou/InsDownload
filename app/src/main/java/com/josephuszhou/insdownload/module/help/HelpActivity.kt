package com.josephuszhou.insdownload.module.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.josephuszhou.base.activity.BaseActivity
import com.josephuszhou.insdownload.R

class HelpActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, HelpActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        setTitle(R.string.help)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
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

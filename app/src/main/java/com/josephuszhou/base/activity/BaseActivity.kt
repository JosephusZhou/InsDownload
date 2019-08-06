package com.josephuszhou.base.activity

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author senfeng.zhou
 * @date 2019-08-06
 * @desc
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
package com.josephuszhou.base.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author senfeng.zhou
 * @date 2019-11-06
 * @desc
 */
abstract class BaseBindingActivity<T: ViewDataBinding> : BaseActivity() {

    protected lateinit var viewDataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId())

        // init your data
        init()

        // set toolbar
        if (goBack()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    @LayoutRes
    protected abstract fun layoutId(): Int

    protected abstract fun init()

    protected open fun goBack() = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
package com.josephuszhou.base.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.josephuszhou.base.widget.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author senfeng.zhou
 * @date 2019-11-06
 * @desc
 */
abstract class Base2Activity<T: ViewDataBinding> : AppCompatActivity(), CoroutineScope by MainScope(), LoadingDialog.DialogControl {

    private var mLoadingDialog: LoadingDialog? = null

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

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoadingDialog() {
        if (mLoadingDialog == null)
            mLoadingDialog = LoadingDialog(this)
        mLoadingDialog?.show()
    }

    override fun hideLoadingDialog() {
        mLoadingDialog?.let {
            if (it.isShowing)
                it.dismiss()
        }
    }
}
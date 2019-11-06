package com.josephuszhou.base.activity

import androidx.appcompat.app.AppCompatActivity
import com.josephuszhou.base.widget.dialog.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author senfeng.zhou
 * @date 2019-08-06
 * @desc
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope(), LoadingDialog.DialogControl {

    private var mLoadingDialog: LoadingDialog? = null

    override fun onDestroy() {
        super.onDestroy()
        cancel()
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
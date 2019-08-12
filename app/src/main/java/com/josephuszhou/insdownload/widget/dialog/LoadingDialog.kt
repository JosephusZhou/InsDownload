package com.josephuszhou.insdownload.widget.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.josephuszhou.insdownload.R

/**
 * @author senfeng.zhou
 * @date 2019-08-12
 * @desc
 */
class LoadingDialog(context: Context) : AlertDialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

        window?.let {
            it.setBackgroundDrawable(ColorDrawable())
            it.attributes.dimAmount = 0.2f
        }
    }

    interface DialogControl {
        fun hideLoadingDialog()
        fun showLoadingDialog()
    }

}
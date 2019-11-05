package com.josephuszhou.insdownload.module.main.activity

import android.Manifest
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.josephuszhou.base.activity.BaseActivity
import com.josephuszhou.insdownload.R
import com.josephuszhou.insdownload.module.about.AboutActivity
import com.josephuszhou.insdownload.module.help.HelpActivity
import com.josephuszhou.insdownload.module.main.adapter.InsAdapter
import com.josephuszhou.insdownload.module.main.entity.InsEntity
import com.josephuszhou.insdownload.module.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val mainPresenter = MainPresenter(this, this)
    private val insAdapter = InsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_get.setOnClickListener(this)
        btn_reset.setOnClickListener(this)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = insAdapter
        insAdapter.onItemChildClickListener = this
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
        getClipData()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private fun getClipData() {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (!clipboardManager.hasPrimaryClip())
            return

        clipboardManager.primaryClipDescription?.let {
            if (!it.hasMimeType(MIMETYPE_TEXT_PLAIN))
                return
            val text = clipboardManager.primaryClip?.getItemAt(0)?.text
            if (text != null) {
                if (text.contains("instagram.com")) {
                    edit_url.setText(text)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_help -> {
                HelpActivity.start(this@MainActivity)
                true
            }
            R.id.action_about -> {
                AboutActivity.start(this@MainActivity)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get -> {
                if (TextUtils.isEmpty(edit_url.text))
                    return
                val insUrl = edit_url.text.toString()
                mainPresenter.requestInsPost(insUrl)
            }
            R.id.btn_reset -> {
                edit_url.setText("")
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val insEntity = insAdapter.getItem(position)
        insEntity?.let {
            if (it.isVideo) {
                it.videoUrl?.let { it1 ->
                    it.downloadStatus = 1
                    insAdapter.notifyDataSetChanged()
                    mainPresenter.requestDownload(it1, it.name, true, position)
                }
            } else {
                it.imageUrl?.let { it1 ->
                    it.downloadStatus = 1
                    insAdapter.notifyDataSetChanged()
                    mainPresenter.requestDownload(it1, it.name, false, position)
                }
            }
        }
    }

    fun onImageListData(imageUrlList: ArrayList<InsEntity>) {
        insAdapter.setNewData(imageUrlList)
    }

    fun onDownloadData(path: String, position: Int) {
        val insEntity = insAdapter.getItem(position)
        insEntity?.let {
            it.localPath = path
            insAdapter.notifyDataSetChanged()
        }
    }

    fun onDownloadError(position: Int) {
        val insEntity = insAdapter.getItem(position)
        insEntity?.let {
            it.downloadStatus = 0
            insAdapter.notifyDataSetChanged()
        }
    }
}

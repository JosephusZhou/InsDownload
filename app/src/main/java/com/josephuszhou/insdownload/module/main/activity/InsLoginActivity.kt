package com.josephuszhou.insdownload.module.main.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import com.josephuszhou.base.activity.BaseActivity
import com.josephuszhou.insdownload.BuildConfig
import com.josephuszhou.insdownload.R
import kotlinx.android.synthetic.main.activity_ins_login.*


class InsLoginActivity : BaseActivity() {

    companion object {
        fun starForResult(activity: Activity, requestCode: Int, loginUrl: String, insUrl: String) {
            activity.startActivityForResult(Intent(activity, InsLoginActivity::class.java).apply {
                putExtra("loginUrl", loginUrl)
                putExtra("insUrl", insUrl)
            }, requestCode)
        }
    }

    private var insUrl: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ins_login)

        insUrl = intent.getStringExtra("insUrl")

        web_view.loadUrl(intent.getStringExtra("loginUrl"))
        web_view.settings.apply {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            userAgentString = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"
        }

        //chrome调试用的  chrome://inspect
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)


        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                Log.e("OkHttp", "web finish url: $url")

                val cookieManager: CookieManager = CookieManager.getInstance()
                val cookieStr: String? = cookieManager.getCookie(url)

                Log.e("OkHttp", "web cookies: $cookieStr")

                cookieStr?.let {
                    if (it.contains("ds_user_id") && it.contains("sessionid")) {
                        Toast.makeText(this@InsLoginActivity, R.string.get_cookies, Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        intent.putExtra("cookies", it)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}

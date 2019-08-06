package com.josephuszhou.insdownload.module.splash

import android.os.Bundle
import com.josephuszhou.base.activity.BaseActivity
import com.josephuszhou.insdownload.R
import com.josephuszhou.insdownload.module.main.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        launch(Dispatchers.IO) {
            delay(500)
            withContext(Dispatchers.Main) {
                MainActivity.start(this@SplashActivity)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                this@SplashActivity.finish()
            }
        }
    }


}

package com.paykids.paykids

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.KakaoSdk
import com.paykids.presentation.view.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Log.d("KeyHash", Utility.getKeyHash(this))
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)

        finish()
    }
}
package com.paykids.paykids

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.paykids.util.LoggerUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        Log.d("KeyHash", Utility.getKeyHash(this))
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        printStartingLog()
    }

    private fun printStartingLog() =
        LoggerUtils.d(this.getString(R.string.app_name) + " Start!")
}
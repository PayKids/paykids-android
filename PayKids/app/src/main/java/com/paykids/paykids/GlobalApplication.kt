package com.paykids.paykids

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.paykids.util.LoggerUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        } catch (e: Exception) {
            LoggerUtils.e("Kakao SDK 초기화 실패: ${e.message}")
        }

        printStartingLog()
    }

    private fun printStartingLog() =
        LoggerUtils.d(this.getString(R.string.app_name) + " Start!")
}
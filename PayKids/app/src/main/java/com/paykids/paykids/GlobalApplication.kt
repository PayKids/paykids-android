package com.paykids.paykids

import android.app.Application
import com.paykids.util.LoggerUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        printStartingLog()
    }

    private fun printStartingLog() =
        LoggerUtil.v(this.getString(R.string.app_name) + " Start!")
}
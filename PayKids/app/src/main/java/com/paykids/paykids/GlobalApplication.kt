package com.paykids.paykids

import android.app.Application
import com.paykids.presentation.utils.LoggerUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        printStartingLog()
    }

    private fun printStartingLog() =
        LoggerUtils.d(this.getString(R.string.app_name) + " Start!")
}
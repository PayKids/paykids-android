package com.paykids.navigation

import android.content.Context
import android.content.Intent

interface Screen {
    fun createIntent(context: Context): Intent
}
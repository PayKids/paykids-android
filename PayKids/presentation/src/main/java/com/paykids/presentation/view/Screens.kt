package com.paykids.presentation.view

import android.content.Context
import android.content.Intent
import com.paykids.navigation.Screen
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.presentation.view.signIn.SignInActivity

enum class Screens : Screen {
    SIGN_IN {
        override fun createIntent(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    },
    HOME {
        override fun createIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    };
}
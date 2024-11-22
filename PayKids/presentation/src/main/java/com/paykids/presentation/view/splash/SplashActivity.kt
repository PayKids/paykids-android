package com.paykids.presentation.view.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.paykids.presentation.base.BaseActivity
import com.paykids.presentation.databinding.ActivityStartBinding
import com.paykids.presentation.view.signIn.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivityStartBinding>() {

    private val handler = Handler(Looper.getMainLooper())

    override fun initView() {
//        // 로그인 상태 확인
//        val isLoggedIn = checkLoginState()
//
//        // 적절한 화면으로 이동
//        val targetScreen = if (isLoggedIn) PresentationScreens.HOME else PresentationScreens.SIGN_IN
//        startActivity(targetScreen.createIntent(this))
//        finish()

        handler.postDelayed({ moveSignIn() }, 1000)
    }

    private fun moveSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

}
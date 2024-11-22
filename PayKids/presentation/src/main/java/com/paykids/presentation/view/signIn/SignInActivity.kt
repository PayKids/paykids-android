package com.paykids.presentation.view.signIn

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.paykids.presentation.base.BaseActivity
import com.paykids.presentation.databinding.ActivitySignInBinding
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    private val handler = Handler(Looper.getMainLooper())

    override fun initView() {
        handler.postDelayed({ moveHome() }, 2000)
    }

    override fun setObserver() {
        super.setObserver()
    }

    private fun moveHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
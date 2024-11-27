package com.paykids.presentation.view.signIn

import android.content.Intent
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseActivity
import com.paykids.presentation.databinding.ActivitySignBinding
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignActivity : BaseActivity<ActivitySignBinding>() {

    override fun initView() {
        val isLoginSuccess = intent.getBooleanExtra("isLoginSuccess", false)

        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.fl_sign, if(isLoginSuccess) SignInNicknameFragment() else SignInProviderFragment())
        ft.replace(R.id.fl_sign, SignInProviderFragment())
        ft.commit()
    }

    fun moveHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
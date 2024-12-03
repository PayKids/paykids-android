package com.paykids.presentation.view.home

import android.content.Intent
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseActivity
import com.paykids.presentation.databinding.ActivityHomeBinding
import com.paykids.presentation.view.mypage.MyPageFragment
import com.paykids.presentation.view.signIn.SignActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun initView() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_home, HomeFragment())
        ft.commit()
    }

    override fun setObserver() {
        super.setObserver()
    }

    fun moveSign() {
        val intent = Intent(this, SignActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.paykids.presentation.view.home

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseActivity
import com.paykids.presentation.databinding.ActivityHomeBinding
import com.paykids.presentation.view.signIn.SignActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private lateinit var navController: NavController

    override fun initView() {
        setContentView(binding.root)
        initNavigation()
    }

    override fun setObserver() {
        super.setObserver()
    }

    fun moveSign() {
        val intent = Intent(this, SignActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.fl_home)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fl_home) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.navBottom.setupWithNavController(navController)
    }
}
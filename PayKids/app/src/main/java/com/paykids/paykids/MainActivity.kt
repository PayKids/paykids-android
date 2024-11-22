package com.paykids.paykids

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paykids.navigation.Screen
import com.paykids.paykids.databinding.ActivityMainBinding
import com.paykids.presentation.view.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)

        finish()
    }
}
package com.paykids.paykids

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paykids.navigation.FragmentNavigator
import com.paykids.navigation.Screen
import com.paykids.paykids.databinding.ActivityMainBinding
import com.paykids.presentation.OneFragment
import com.paykids.presentation.TwoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentNavigator {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            navigateTo(Screen.ONE) // 초기 화면 설정
        }
    }

    override fun navigateTo(screen: Screen) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val fragment = when (screen) {
            Screen.ONE -> OneFragment()
            Screen.TWO -> TwoFragment()
        }

        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
package com.paykids.presentation.view.quest

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentQuestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestFragment : BaseFragment<FragmentQuestBinding>() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initView() {
        hideStatusBar()
        setStatusBarColorLight()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QuestPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.text_tab_quest)
                1 -> getString(R.string.text_tab_achievement)
                else -> null
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColorLight()
    }
}

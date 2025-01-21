package com.paykids.presentation.view.quest

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class QuestPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> QuestListFragment()
            1 -> AchievementFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
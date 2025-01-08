package com.paykids.presentation.view.diary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paykids.presentation.view.OnRvItemClickListener
import java.util.Calendar

class DiaryMonthCalendarStateAdapter(
    fa: FragmentActivity,
    private val onDateClickListener: OnRvItemClickListener<String>
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        return DiaryMonthFragment.newInstance(calendar.time).apply {
            setOnDateClickListener(onDateClickListener)
        }
    }
}
package com.paykids.presentation.view.diary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paykids.presentation.view.OnRvItemClickListener
import java.util.Calendar

class DiaryMonthCalendarStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private var dateClickListener: OnRvItemClickListener<String>? = null
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        return DiaryMonthFragment.newInstance(calendar.time)
    }

    // 날짜 클릭 리스너 설정 메서드
    fun setFragmentDateClickListener(listener: OnRvItemClickListener<String>) {
        dateClickListener = listener
    }
}
package com.paykids.presentation.view.diary

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.paykids.domain.model.DayInfo
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentDiaryMonthBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.OnRvItemClickListener
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class DiaryMonthFragment : BaseFragment<FragmentDiaryMonthBinding>() {
    private val viewModel: DiaryViewModel by viewModels()
    private lateinit var dayAdapter: DiaryDayCalendarAdapter
    private lateinit var date: Date

    private var dateClickListener: OnRvItemClickListener<String>? = null

    companion object {
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): DiaryMonthFragment {
            val fragment = DiaryMonthFragment()
            val args = Bundle()
            args.putLong(ARG_DATE, date.time)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {
        date = arguments?.getLong(ARG_DATE)?.let { Date(it) } ?: Date()

        val daysInMonth = getDaysInMonth(date)
        val initialList =
            daysInMonth.map { day -> Pair(day, null as DayInfo?) } // 초기 상태로 DayInfo는 null로 설정
        dayAdapter = DiaryDayCalendarAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<Int> {
                override fun onClick(day: Int) {
                    val clickedDate = getDateStringForDay(day) // 선택된 날짜를 문자열 형식으로 변환
                    dateClickListener?.onClick(clickedDate)
                }
            })
        }
        dayAdapter.submitList(initialList)

        binding.rvCalendarDays.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarDays.adapter = dayAdapter
        binding.rvCalendarDays.itemAnimator = null
    }

    private fun getDateStringForDay(day: Int): String {
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.DAY_OF_MONTH, day)
        }
        return "${calendar.get(Calendar.YEAR)}-${
            (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        }-${day.toString().padStart(2, '0')}"
    }

    private fun getDaysInMonth(date: Date): List<String> {
        val calendar = Calendar.getInstance().apply { time = date }
        val daysInMonth = mutableListOf<String>()

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        for (i in 0 until firstDayOfWeek) {
            daysInMonth.add("previous")
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            daysInMonth.add(i.toString())
        }

        return daysInMonth
    }

    fun setDateClickListener(listener: OnRvItemClickListener<String>) {
        dateClickListener = listener
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.diaryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    LoggerUtils.e(state.message)
                }

                is UiState.Success -> {
//                    dayAdapter.submitList(
//                        state.data,
//                        getDaysInMonth(date)
//                    )
                }
            }
        }
    }
}
package com.paykids.presentation.view.diary

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.paykids.domain.model.DiaryInfo
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
        dayAdapter = DiaryDayCalendarAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<Int> {
                override fun onClick(item: Int) {
                    val clickedDate =
                        "${date.year}-${date.month}-${item.toString().padStart(2, '0')}"
                    viewModel.fetchDetailsForDate(clickedDate)
                }
            })
        }
        dayAdapter.submitList(
            matchDiaryEntriesWithDays(
                List<DiaryInfo?>(daysInMonth.size) { null },
                daysInMonth
            )
        )

        binding.rvCalendarDays.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarDays.adapter = dayAdapter
        binding.rvCalendarDays.itemAnimator = null
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

    private fun matchDiaryEntriesWithDays(
        diaryInfos: List<DiaryInfo?>,
        days: List<String>
    ): List<Pair<String, DiaryInfo?>> {
        val result = mutableListOf<Pair<String, DiaryInfo?>>()
        val datePattern = """\d{4}-\d{2}-(\d{2})""".toRegex()

        for (day in days) {
            if (day == "previous" || day == "next") {
                result.add(day to null)
            } else {
                val dayWithLeadingZero = day.padStart(2, '0')
                val matchedDiaryInfo = diaryInfos.find { diaryInfo ->
                    val entryDate = diaryInfo?.diaryEntryDate
                    entryDate?.let { datePattern.find(it)?.groupValues?.get(1) == dayWithLeadingZero }
                        ?: false
                }
                result.add(day to matchedDiaryInfo)
            }
        }

        return result
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
                    dayAdapter.submitList(
                        matchDiaryEntriesWithDays(
                            state.data,
                            getDaysInMonth(date)
                        )
                    )
                }
            }
        }
    }
}
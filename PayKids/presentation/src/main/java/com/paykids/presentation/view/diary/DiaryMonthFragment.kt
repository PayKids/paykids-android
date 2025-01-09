package com.paykids.presentation.view.diary

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentDiaryMonthBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.OnRvItemClickListener
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class DiaryMonthFragment : BaseFragment<FragmentDiaryMonthBinding>() {
    private val viewModel: DiaryViewModel by activityViewModels()
    private lateinit var dayAdapter: DiaryDayCalendarAdapter
    private lateinit var date: Date

    private var onDateClickListener: OnRvItemClickListener<String>? = null

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
        val today = getToday()
        val currentYear = today.split("-")[0].toInt()
        val currentMonth = today.split("-")[1].toInt()
//        val daysInMonth = getDaysInMonth(date)
//        val initialList =
//            daysInMonth.map { day -> Pair(day, null as MonthDailyInfo?) }

        viewModel.getMonthDailyExpense(currentYear, currentMonth)
        viewModel.getMonthDailyIncome(currentYear, currentMonth)

        dayAdapter = DiaryDayCalendarAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<Int> {
                override fun onClick(item: Int) {
                    val clickedDate = getDateStringForDay(item)
                    onDateClickListener?.onClick(clickedDate)
                    viewModel.getDayExpense(clickedDate)
                }
            })
        }

        binding.rvCalendarDays.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarDays.adapter = dayAdapter
        binding.rvCalendarDays.itemAnimator = null
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.monthDailyExpenseState.observe(viewLifecycleOwner) { expenseState ->
            viewModel.monthDailyIncomeState.observe(viewLifecycleOwner) { incomeState ->
                if (expenseState is UiState.Success && incomeState is UiState.Success) {

                    val expenseData = expenseState.data
                    val incomeData = incomeState.data

                    val updatedList = getDaysInMonth(date).map { day ->
                        val expenseInfo = expenseData.find { it.date == day }
                        val incomeInfo = incomeData.find { it.date == day }
                        Pair(day, Pair(expenseInfo, incomeInfo))
                    }

                    dayAdapter.submitList(updatedList)
                }
            }
        }

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

        val year = calendar.get(Calendar.YEAR)
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        for (i in 0 until firstDayOfWeek) {
            daysInMonth.add("previous")
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            val day = i.toString().padStart(2, '0')
            daysInMonth.add("$year-$month-$day")
        }

        return daysInMonth
    }

    fun setOnDateClickListener(listener: OnRvItemClickListener<String>) {
        onDateClickListener = listener
    }

    private fun getToday(): String {
        val today = Calendar.getInstance().run {
            val year = get(Calendar.YEAR)
            val month = (get(Calendar.MONTH) + 1).toString().padStart(2, '0')
            val day = get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
            "$year-$month-$day"
        }
        return today
    }

    private fun updateDayDetails(details: List<DayInfo>) {
//        val updatedList = dayAdapter.currentList.map { pair ->
//            val updatedInfo = details.find { it. == pair.first }
//            pair.copy(second = updatedInfo)
//        }
//        dayAdapter.submitList(updatedList)
    }
}
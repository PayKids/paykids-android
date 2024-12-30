package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.paykids.domain.model.DayInfo
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentDiaryBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.Constants.formatDateToKorean
import com.paykids.presentation.view.OnRvItemClickListener
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(), ConfirmDialogInterface {
    private val viewModel: DiaryViewModel by activityViewModels()
    private lateinit var calendarAdapter: DiaryMonthCalendarStateAdapter
    private lateinit var detailAdapter: DetailConsumeAdapter

    private var currentMonth = 0

    override fun initView() {
        viewModel.fetchMonthlyData()

        detailAdapter = DetailConsumeAdapter(this)
        binding.rvDetailConsume.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            this.adapter = detailAdapter
        }

        viewModel.selectedDateDetails.observe(viewLifecycleOwner) { details ->
            detailAdapter.submitList(details)
        }

        calendarAdapter = DiaryMonthCalendarStateAdapter(
            requireActivity(),
            object : OnRvItemClickListener<String> {
                override fun onClick(item: String) {
                    updateSelectDayText(item)
                }
            })

        binding.vpCalendarMonth.adapter = calendarAdapter
        binding.vpCalendarMonth.setCurrentItem(Int.MAX_VALUE / 2, false)
        binding.vpCalendarMonth.offscreenPageLimit = 1

        // viewPager 스크롤 막기
        binding.vpCalendarMonth.getChildAt(0).setOnTouchListener { _, _ -> true }

        val today = getToday()
        binding.vpCalendarMonth.post {
            updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
        }
        updateSelectDayText(today)
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.ivConsumptionStatus.setOnClickListener {
            val calendar = Calendar.getInstance().apply {
                add(Calendar.MONTH, binding.vpCalendarMonth.currentItem - (Int.MAX_VALUE / 2))
            }
            val currentMonth = SimpleDateFormat("yyyy-MM", Locale.KOREAN).format(calendar.time)

            val bundle = Bundle().apply {
                putString("currentMonth", currentMonth)
            }

            navController.navigate(R.id.analysisConsumeFragment, bundle)
        }

        binding.ibAddPocketMoney.setOnClickListener {
            val dialog = DiaryDialog()
            dialog.isCancelable = true
            dialog.show(parentFragmentManager, "AddPocketMoneyDialog")
        }

        binding.vpCalendarMonth.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateCurrentMonthText(position)
            }
        })

        binding.ibLeft.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
            currentMonth -= 1
            binding.vpCalendarMonth.setCurrentItem(currentPos - 1, false)
        }

        binding.ibRight.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
            currentMonth += 1
            binding.vpCalendarMonth.setCurrentItem(currentPos + 1, false)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.selectedDateDetails.observe(viewLifecycleOwner) { details ->
            detailAdapter.submitList(details)
        }
    }

    override fun onYesButtonClick() {

    }

    @SuppressLint("SetTextI18n")
    private fun updateCurrentMonthText(position: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.KOREAN).format(calendar.time)
        binding.tvMonth.text = "${calendar.get(Calendar.MONTH) + 1}월"

        val totalConsume = viewModel.getMonthConsumption(yearMonth)
        binding.tvMonthConsumption.text = "${Constants.formatAmount(totalConsume)}원 사용 중"

        viewModel.fetchDetailsForDate(yearMonth)
        getMostConsumedCategoryForMonth(yearMonth)
    }

    private fun getMostConsumedCategoryForMonth(yearMonth: String) {
        val mostConsumedCategory = viewModel.getMostConsumedCategoryForMonth(yearMonth)
        mostConsumedCategory?.let { (place, totalAmount) ->
            val month = yearMonth.split("-")[1] + "월"
            binding.tvConsumptionMost.text =
                getString(R.string.text_month_most_consume, month, place)
            binding.tvMostConsumeCategoryAmount.text = Constants.formatAmount(totalAmount)
        }
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

    private fun updateSelectDayText(day: String) {
        val formattedDate = formatDateToKorean(day)
        binding.tvSelectDay.text = formattedDate
    }
}
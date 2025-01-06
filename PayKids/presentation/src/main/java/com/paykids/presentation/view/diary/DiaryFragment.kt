package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.paykids.domain.model.DetailConsume
import com.paykids.domain.model.DetailTransaction
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.DialogDiaryBinding
import com.paykids.presentation.databinding.FragmentDiaryBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.Constants.formatDateToKorean
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.OnRvItemClickListener
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(), ConfirmDialogInterface {
    private val viewModel: DiaryViewModel by activityViewModels()
    private lateinit var calendarAdapter: DiaryMonthCalendarStateAdapter
    private lateinit var detailAdapter: DetailConsumeAdapter

    private var currentMonth = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        viewModel.getMonthTotalExpense(2025, 1)
        viewModel.getMonthDailyExpense(2025, 1)

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
            showAddPocketMoneyDialog("2025-01-01")
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

        viewModel.monthTotalExpenseState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("월 전체 소비 금액 조회 성공: ${it.data}")
                }
            }
        }

        viewModel.monthDailyExpenseState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("월 일별 소비 금액 조회 성공: ${it.data}")
                }
            }
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

//        val totalConsume = viewModel.getMonthConsumption(yearMonth)
//        binding.tvMonthConsumption.text = "${Constants.formatAmount(totalConsume)}원 사용 중"

        viewModel.fetchDetailsForDate(yearMonth)
        getMostConsumedCategoryForMonth(yearMonth)
    }

    private fun getMostConsumedCategoryForMonth(yearMonth: String) {
//        val mostConsumedCategory = viewModel.getMostConsumedCategoryForMonth(yearMonth)
//        mostConsumedCategory?.let { (place, totalAmount) ->
//            val month = yearMonth.split("-")[1] + "월"
//            binding.tvConsumptionMost.text =
//                getString(R.string.text_month_most_consume, month, place)
//            binding.tvMostConsumeCategoryAmount.text = Constants.formatAmount(totalAmount)
//        }
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

    @SuppressLint("SetTextI18n")
    private fun showAddPocketMoneyDialog(today: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogDiaryBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var selectedDate = LocalDate.parse(today, dateFormatter)
        val currentYear = selectedDate.year
        val currentMonth = selectedDate.monthValue
        val currentDay = selectedDate.dayOfMonth
        binding.tvYear.text = "${currentYear}년"
        binding.tvMonth.text = "${currentMonth}월"
        binding.tvDay.text = "${currentDay}일"

        val items = resources.getStringArray(R.array.category_array)
        val adapter = CustomSpinnerAdapter(requireContext(), items)
        binding.spinnerCategory.adapter = adapter

        binding.ivYearUp.setOnClickListener {
            selectedDate = selectedDate.plusYears(1)
            binding.tvYear.text = "${selectedDate.year}년"
        }

        binding.ivYearDown.setOnClickListener {
            selectedDate = selectedDate.minusYears(1)
            binding.tvYear.text = "${selectedDate.year}년"
        }

        binding.ivMonthUp.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            binding.tvMonth.text = "${selectedDate.monthValue}월"
        }

        binding.ivMonthDown.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            binding.tvMonth.text = "${selectedDate.monthValue}월"
        }

        binding.ivDayUp.setOnClickListener {
            selectedDate = selectedDate.plusDays(1)
            binding.tvDay.text = "${selectedDate.dayOfMonth}일"
        }

        binding.ivDayDown.setOnClickListener {
            selectedDate = selectedDate.minusDays(1)
            binding.tvDay.text = "${selectedDate.dayOfMonth}일"
        }

        binding.etAddAmount.setText("")
        binding.etMemo.setText("")

        binding.btnSubmit.setOnClickListener {
            val amount = binding.etAddAmount.text.toString().toIntOrNull() ?: 0
            val memo = binding.etMemo.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (amount <= 0) {
                showToast("금액을 입력해주세요")
            } else {
                val detailConsume = DetailConsume(formattedDate, category, amount, memo)
//                viewModel.addTransaction(detailConsume)
//
//                viewModel.fetchMonthlyData()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(transactions: List<DetailTransaction>) {
        detailAdapter.submitList(transactions.filterIsInstance<DetailConsume>())

        val totalConsume = transactions.filterIsInstance<DetailConsume>().sumOf { it.amount }
        binding.tvMonthConsumption.text = "${Constants.formatAmount(totalConsume)}원 사용 중"
    }
}
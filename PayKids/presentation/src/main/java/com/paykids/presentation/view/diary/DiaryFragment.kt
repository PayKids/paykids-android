package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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

    private var today: String = ""
    private var currentYear: Int = 0
    private var currentMonth: Int = 0
    private var currentDay: Int = 0

    override fun initView() {

        today = getToday()
        currentYear = today.split("-")[0].toInt()
        currentMonth = today.split("-")[1].toInt()
        currentDay = today.split("-")[2].toInt()

        fetchData(currentYear, currentMonth)
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.ivConsumptionStatus.setOnClickListener {
            val action = DiaryFragmentDirections.actionDiaryFragmentToAnalysisConsumeFragment(
                currentYear = currentYear,
                currentMonth = currentMonth
            )
            navController.navigate(action)
        }

        binding.ibAddPocketMoney.setOnClickListener {
            showAddPocketMoneyDialog(today)
        }

        binding.vpCalendarMonth.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateCurrentMonthText(position)
            }
        })

        binding.ibLeft.setOnClickListener {
            minusMonth()
            viewModel.updateMonth(currentYear, currentMonth)
            viewModel.getMonthMostCategory(currentYear, currentMonth)
            val currentPos = binding.vpCalendarMonth.currentItem
            binding.vpCalendarMonth.setCurrentItem(currentPos - 1, false)
        }

        binding.ibRight.setOnClickListener {
            plusMonth()
            viewModel.updateMonth(currentYear, currentMonth)
            viewModel.getMonthMostCategory(currentYear, currentMonth)
            val currentPos = binding.vpCalendarMonth.currentItem
            binding.vpCalendarMonth.setCurrentItem(currentPos + 1, false)
        }
    }

    @SuppressLint("SetTextI18n")
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
                    binding.tvMonthConsumption.text = "${Constants.formatAmount(it.data)}원 사용 중"
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

        viewModel.monthMostCategoryState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("월 최대 소비 카테고리, 금액 조회 성공: ${it.data}")
                    fetchMonthMostCategoryInfo(it.data.category, it.data.amount)
                }
            }
        }

        viewModel.dayExpenseState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("일별 소비 내역 조회 성공: ${it.data}")
                    detailAdapter = DetailConsumeAdapter(this)
                    binding.rvDetailConsume.apply {
                        layoutManager =
                            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                        this.adapter = detailAdapter
                    }
                    detailAdapter.submitList(it.data)
                }
            }
        }

        viewModel.addExpenseState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("소비 내역 저장 성공: ${it.data}")
                    showToast("소비 내역 저장 성공")
                }
            }
        }

        viewModel.addIncomeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("수입 내역 저장 성공: ${it.data}")
                    showToast("수입 내역 저장 성공")
                }
            }
        }
    }

    override fun onYesButtonClick() {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun fetchData(year: Int, month: Int) {

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

        binding.vpCalendarMonth.post {
            updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
        }

        updateSelectDayText(today)
        viewModel.getMonthMostCategory(year, month)
        viewModel.getDayExpense(today)
    }

    @SuppressLint("SetTextI18n")
    private fun updateCurrentMonthText(position: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.KOREAN).format(calendar.time)
        val year = yearMonth.split("-")[0].toInt()
        val month = yearMonth.split("-")[1].toInt()
        binding.tvMonth.text = "${calendar.get(Calendar.MONTH) + 1}월"

        viewModel.getMonthTotalExpense(year, month)
    }

    private fun fetchMonthMostCategoryInfo(category: String, amount: Int) {
        val blueColor = ContextCompat.getColor(requireContext(), R.color.blue1)
        val fullText = getString(R.string.text_month_most_consume, currentMonth, category)

        val startIndex = fullText.indexOf(category)
        val endIndex = startIndex + category.length

        val spannable = SpannableString(fullText).apply {
            setSpan(
                ForegroundColorSpan(blueColor),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.tvConsumptionMost.text = spannable
        binding.tvMostConsumeCategoryAmount.text = Constants.formatAmount(amount)
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

    private fun minusMonth() {
        if (currentMonth == 1) {
            currentMonth = 12
            currentYear -= 1
        } else {
            currentMonth -= 1
        }
    }

    private fun plusMonth() {
        if (currentMonth == 12) {
            currentMonth = 1
            currentYear += 1
        } else {
            currentMonth += 1
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAddPocketMoneyDialog(date: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogDiaryBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var selectedDate = LocalDate.parse(date, dateFormatter)
        var isExpenseSelected = true
        val currentYear = selectedDate.year
        val currentMonth = selectedDate.monthValue
        val currentDay = selectedDate.dayOfMonth
        binding.tvYear.text = "${currentYear}년"
        binding.tvMonth.text = "${currentMonth}월"
        binding.tvDay.text = "${currentDay}일"

        fun fetchCategory() {
            LoggerUtils.d(isExpenseSelected.toString())
            if (isExpenseSelected) {
                viewModel.getExpenseCategory()
            } else {
                viewModel.getIncomeCategory()
            }
        }

        viewModel.getExpenseCategoryState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.spinnerCategory.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.spinnerCategory.visibility = View.VISIBLE
                    val categories = uiState.data
                    val customAdapter = CustomSpinnerAdapter(
                        requireContext(),
                        categories.map { it.category }.toTypedArray()
                    )
                    binding.spinnerCategory.adapter = customAdapter
                }

                is UiState.Failure -> {
                    binding.spinnerCategory.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "카테고리 조회 실패: ${uiState.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.getIncomeCategoryState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.spinnerCategory.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.spinnerCategory.visibility = View.VISIBLE
                    val categories = uiState.data
                    val customAdapter = CustomSpinnerAdapter(
                        requireContext(),
                        categories.map { it.category }.toTypedArray()
                    )
                    binding.spinnerCategory.adapter = customAdapter
                }

                is UiState.Failure -> {
                    binding.spinnerCategory.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "수입 카테고리 조회 실패: ${uiState.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        fetchCategory()

        binding.clSwitch.setOnClickListener {
            isExpenseSelected = !isExpenseSelected
            if (isExpenseSelected) {
                binding.tvConsume.setBackgroundResource(R.drawable.switch_bg_select)
                binding.tvConsume.setTextColor(requireContext().getColor(R.color.black))

                binding.tvIncome.setBackgroundResource(R.color.transparent)
                binding.tvIncome.setTextColor(requireContext().getColor(R.color.gray7))
            } else {
                binding.tvIncome.setBackgroundResource(R.drawable.switch_bg_select)
                binding.tvIncome.setTextColor(requireContext().getColor(R.color.black))

                binding.tvConsume.setBackgroundResource(R.color.transparent)
                binding.tvConsume.setTextColor(requireContext().getColor(R.color.gray7))
            }
            fetchCategory()
        }

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
                if (isExpenseSelected) {
                    viewModel.addExpense(formattedDate, "EXPENSE", amount, memo, category)
                } else {
                    viewModel.addIncome(formattedDate, "INCOME", amount, memo, category)
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }
}
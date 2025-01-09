package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisConsumeBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisConsumeFragment : BaseFragment<FragmentAnalysisConsumeBinding>() {
    private val viewModel: DiaryViewModel by activityViewModels()

    private var categories = mutableListOf<String>()
    private lateinit var adapter: AllowanceCategoryAdapter
    private var isDeleteMode = false
    private var isConsumeSelected = true
    private var currentYear: Int = 0
    private var currentMonth: Int = 0

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val args = AnalysisConsumeFragmentArgs.fromBundle(requireArguments())
        currentYear = args.currentYear
        currentMonth = args.currentMonth
        binding.tvMonth.text = "${currentMonth}월"

        fetchData(currentYear, currentMonth)
        updateDeleteButtonVisibility(categories)
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.clSwitch.setOnClickListener {
            toggleSwitch()
        }

        binding.ibLeft.setOnClickListener {
            minusMonth()
        }

        binding.ibRight.setOnClickListener {
            plusMonth()
        }

        binding.tvDelete.setOnClickListener {
            toggleDeleteMode()
        }

        binding.btnAddCategory.setOnClickListener {
//            adapter.addCategoryInput()
//            binding.rvDetailConsume.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            "CATEGORY_BACK_RESULT",
            viewLifecycleOwner
        ) { _, bundle ->
            val year = bundle.getInt("year")
            val month = bundle.getInt("month")

            currentYear = year
            currentMonth = month
            binding.tvMonth.text = "${currentMonth}월"
            fetchData(currentYear, currentMonth)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchData(year: Int, month: Int) {
        LoggerUtils.d(isConsumeSelected.toString())
        if (isConsumeSelected) {
            viewModel.getMonthTotalExpense(year, month)
            viewModel.getMonthAllExpenseCategory(year, month)
        } else {
            viewModel.getMonthTotalIncome(year, month)
            viewModel.getMonthAllIncomeCategory(year, month)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setObserver() {
        super.setObserver()

        viewModel.allExpenseCategoryState.observe(viewLifecycleOwner) { state ->
            handleCategoryState(state)
        }

        viewModel.allIncomeCategoryState.observe(viewLifecycleOwner) { state ->
            handleCategoryState(state)
        }

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

    }

    private fun handleCategoryState(state: UiState<List<MonthAllCategoryInfo>>) {
        when (state) {
            is UiState.Failure -> {
                showToast(state.message)
            }

            is UiState.Loading -> {}

            is UiState.Success -> {
                LoggerUtils.d("카테고리 조회 성공: ${state.data}")

                if (!::adapter.isInitialized) {
                    adapter = AllowanceCategoryAdapter(
                        onCategoryAdded = { newCategory ->
                            addCategory(newCategory)
                        },
                        onItemClick = { category, amount ->
                            val action = AnalysisConsumeFragmentDirections
                                .actionAnalysisConsumeFragmentToAnalysisCategoryConsumeFragment(
                                    currentYear,
                                    currentMonth,
                                    category,
                                    amount
                                )
                            findNavController().navigate(action)
                        }
                    )
                    binding.rvDetailConsume.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvDetailConsume.adapter = adapter
                }

                val sortedCategories = state.data
                    .sortedByDescending { it.percent.replace("%", "").toFloat() }
                    .map {
                        AllowanceCategoryAdapter.CategoryItem.Normal(
                            name = it.category,
                            amount = it.amount,
                            percent = it.percent
                        )
                    }

                adapter.submitList(sortedCategories)

                val topCategories = state.data
                    .sortedByDescending { it.percent.replace("%", "").toFloat() }
                    .take(3)
                val colors = mutableListOf(
                    ContextCompat.getColor(requireContext(), R.color.blue1),
                    ContextCompat.getColor(requireContext(), R.color.blue2),
                    ContextCompat.getColor(requireContext(), R.color.blue3)
                )

                while (colors.size < state.data.size) {
                    colors.add(Color.LTGRAY)
                }

                binding.categoryProgressView.updateSections(
                    state.data.map { it.percent.replace("%", "").toFloat() },
                    colors,
                    topCategories.map { it.category }
                )
            }
        }
    }

    private fun updateDeleteButtonVisibility(items: List<String>) {
        if (items.isEmpty()) {
            binding.flDelete.visibility = View.GONE
        } else {
            binding.flDelete.visibility = View.VISIBLE
        }
    }

    private fun addCategory(newCategory: String) {
        // 새 카테고리 추가
        categories.add(newCategory)

        // 어댑터에 새로운 항목 추가
        val currentList = adapter.currentList.toMutableList()
        currentList.add(
            currentList.size - 1,
            AllowanceCategoryAdapter.CategoryItem.Normal(newCategory, false, 0, "0")
        )
        adapter.submitList(currentList)

        // 삭제 버튼 가시성 업데이트
        updateDeleteButtonVisibility(categories)
    }

    private fun deleteSelectedItems() {
        val deletedItems = adapter.deleteSelectedItems()
        categories.removeAll { it in deletedItems }
        updateDeleteButtonVisibility(categories)
    }

    private fun toggleDeleteMode() {
        isDeleteMode = !isDeleteMode
        adapter.toggleDeleteMode(isDeleteMode)

        if (isDeleteMode) {
            binding.tvDelete.text = "삭제"
            binding.btnAddCategory.isEnabled = false
        } else {
            binding.tvDelete.text = "카테고리 삭제"
            binding.btnAddCategory.isEnabled = true
            deleteSelectedItems()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun minusMonth() {
        if (currentMonth == 1) {
            currentMonth = 12
            currentYear -= 1
        } else {
            currentMonth -= 1
        }
        binding.tvMonth.text = "${currentMonth}월"
        fetchData(currentYear, currentMonth)
    }

    @SuppressLint("SetTextI18n")
    private fun plusMonth() {
        if (currentMonth == 12) {
            currentMonth = 1
            currentYear += 1
        } else {
            currentMonth += 1
        }
        binding.tvMonth.text = "${currentMonth}월"
        fetchData(currentYear, currentMonth)
    }

    private fun toggleSwitch() {
        isConsumeSelected = !isConsumeSelected

        if (isConsumeSelected) {
            // 소비가 선택된 경우
            binding.tvConsume.setBackgroundResource(R.drawable.switch_bg_select)
            binding.tvConsume.setTextColor(requireContext().getColor(R.color.black))

            binding.tvIncome.setBackgroundResource(R.color.transparent)
            binding.tvIncome.setTextColor(requireContext().getColor(R.color.gray7))

            viewModel.getMonthAllExpenseCategory(currentYear, currentMonth)
        } else {
            // 수입이 선택된 경우
            binding.tvIncome.setBackgroundResource(R.drawable.switch_bg_select)
            binding.tvIncome.setTextColor(requireContext().getColor(R.color.black))

            binding.tvConsume.setBackgroundResource(R.color.transparent)
            binding.tvConsume.setTextColor(requireContext().getColor(R.color.gray7))

            viewModel.getMonthAllIncomeCategory(currentYear, currentMonth)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)
        fetchData(currentYear, currentMonth)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }
}
package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisAllowanceBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisConsumeFragment : BaseFragment<FragmentAnalysisAllowanceBinding>() {
    private val viewModel: DiaryViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private var categories = mutableListOf<String>()
    private lateinit var adapter: AllowanceCategoryAdapter
    private var isDeleteMode = false
    private var isConsumeSelected = true
    private var currentList: MutableList<AllowanceCategoryAdapter.CategoryItem>? = null
    private var currentYear: Int = 0
    private var currentMonth: Int = 0

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val args = AnalysisConsumeFragmentArgs.fromBundle(requireArguments())
        currentYear = args.currentYear
        currentMonth = args.currentMonth
        binding.tvMonth.text = "${currentMonth}월"

        initializeAdapter()
        fetchData(currentYear, currentMonth)
        setupFragmentResultListener()
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
            adapter.addCategoryInput()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()
    }

    @SuppressLint("SetTextI18n")
    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            "CATEGORY_BACK_RESULT",
            viewLifecycleOwner
        ) { _, bundle ->
            val year = bundle.getInt("year")
            val month = bundle.getInt("month")

            currentYear = year
            currentMonth = month
            binding.tvMonth.text = "${currentMonth}월"

            if (!::adapter.isInitialized) {
                initializeAdapter()
            }
        }
    }

    private fun initializeAdapter() {
        adapter = AllowanceCategoryAdapter(
            categoryViewModel,
            onCategoryAdded = { newCategory ->
                addCategory(newCategory)
            },
            onItemClick = { category, amount ->
                val action = AnalysisConsumeFragmentDirections
                    .actionAnalysisConsumeFragmentToAnalysisCategoryConsumeFragment(
                        currentYear,
                        currentMonth,
                        category,
                        amount,
                        isConsumeSelected
                    )
                findNavController().navigate(action)
            },
            isConsumeSelected
        )
        binding.rvDetailAllowance.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetailAllowance.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun fetchData(year: Int, month: Int) {
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
                    binding.tvMonthAllowance.text = "${Constants.formatAmount(it.data)}원 사용 중"
                }
            }
        }

        viewModel.monthTotalIncomeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("월 전체 수입 금액 조회 성공: ${it.data}")
                    binding.tvMonthAllowance.text = "${Constants.formatAmount(it.data)}원 수입 중"
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
                    initializeAdapter()
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

                currentList = sortedCategories.toMutableList()
                adapter.submitList(currentList)

                val topCategories = state.data
                    .sortedByDescending { it.percent.replace("%", "").toFloat() }
                    .take(3)
                val percentages = topCategories.map { it.percent.replace("%", "").toFloat() / 100 }
                val colors = mutableListOf(
                    ContextCompat.getColor(requireContext(), R.color.blue1),
                    ContextCompat.getColor(requireContext(), R.color.blue2),
                    ContextCompat.getColor(requireContext(), R.color.blue3)
                )
                val categoryNames = topCategories.map { it.category }
                binding.categoryProgressView.updateSections(percentages, colors, categoryNames)

                updateDeleteButtonVisibility(state.data.map { it.category })
            }
        }
    }

    private fun updateDeleteButtonVisibility(items: List<String>) {
        binding.flDelete.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun addCategory(newCategory: String) {
        categories.add(newCategory)

        val currentList = adapter.currentList.toMutableList()
        currentList.add(
            currentList.size - 1,
            AllowanceCategoryAdapter.CategoryItem.Normal(newCategory, false, 0, "0")
        )
        adapter.submitList(currentList)

        updateDeleteButtonVisibility(categories)
    }

    private fun  deleteExpenseItems(): List<String> {
        val selectedItems = adapter.getSelectedCategories()
        val selectedNames = selectedItems.map { it.name }

        selectedNames.forEach { categoryName ->
            categoryViewModel.deleteExpenseCategory(categoryName)
        }

        currentList!!.removeAll(selectedItems)
        adapter.submitList(currentList)
        return selectedNames
    }

    private fun deleteIncomeItems(): List<String> {
        val selectedItems = adapter.getSelectedCategories()
        val selectedNames = selectedItems.map { it.name }

        selectedNames.forEach { categoryName ->
            categoryViewModel.deleteIncomeCategory(categoryName)
        }

        currentList!!.removeAll(selectedItems)
        adapter.submitList(currentList)
        return selectedNames
    }

    private fun toggleDeleteMode() {
        isDeleteMode = !isDeleteMode
        adapter.toggleDeleteMode(isDeleteMode)

        if (isDeleteMode) {
            binding.tvDelete.text = "삭제"
            binding.btnAddCategory.isEnabled = false

//            if (isConsumeSelected) {
//                deleteExpenseItems()
//            } else {
//                deleteIncomeItems()
//            }
        } else {
            binding.tvDelete.text = "카테고리 삭제"
            binding.btnAddCategory.isEnabled = true
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

            fetchData(currentYear, currentMonth)
        } else {
            // 수입이 선택된 경우
            binding.tvIncome.setBackgroundResource(R.drawable.switch_bg_select)
            binding.tvIncome.setTextColor(requireContext().getColor(R.color.black))

            binding.tvConsume.setBackgroundResource(R.color.transparent)
            binding.tvConsume.setTextColor(requireContext().getColor(R.color.gray7))

            fetchData(currentYear, currentMonth)
        }

        if (::adapter.isInitialized) {
            adapter.updateMode(isConsumeSelected)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }
}
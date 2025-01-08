package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val items = mutableListOf("편의점", "편", "의점", "편의점편", "편의점편의")
    private lateinit var adapter: ConsumeCategoryAdapter
    private var isDeleteMode = false
    private var currentYear: Int = 0
    private var currentMonth: Int = 0

    @SuppressLint("SetTextI18n")
    override fun initView() {

        val args = AnalysisConsumeFragmentArgs.fromBundle(requireArguments())
        val currentYear = args.currentYear
        val currentMonth = args.currentMonth
        binding.tvMonth.text = "${currentMonth}월"

        fetchData(currentYear, currentMonth)

        adapter = ConsumeCategoryAdapter(
            onCategoryAdded = { newCategory ->
                addCategory(newCategory)
            },
            onItemClick = { category, amount ->
                navigateToAnalysisConsumeLocationFragment(category, amount)
            }
        )

        binding.rvDetailConsume.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetailConsume.adapter = adapter

        updateDeleteButtonVisibility(items)
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ibLeft.setOnClickListener {
            minusMonth()
            fetchData(currentYear, currentMonth)
        }

        binding.ibRight.setOnClickListener {
            plusMonth()
            fetchData(currentYear, currentMonth)
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
    private fun fetchData(year: Int, month: Int) {
        viewModel.getMonthTotalExpense(year, month)
        viewModel.getMonthAllCategory(year, month)
        viewModel.getMonthAllCategory(year, month)
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

        viewModel.allCategoryState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("월 전체 카테고리 조회 성공: ${it.data}")

                    if (::adapter.isInitialized) {
                        val sortedCategories = it.data
                            .sortedByDescending { it.percent }
                            .map {
                                ConsumeCategoryAdapter.CategoryItem.Normal(
                                    name = it.category,
                                    amount = it.amount.toString(),
                                    percent = it.percent
                                )
                            }

                        adapter.submitList(sortedCategories)
                    }

                    val topCategories = it.data
                        .sortedByDescending { it.percent }
                        .take(3)
                    val colors = mutableListOf(
                        ContextCompat.getColor(requireContext(), R.color.blue1),
                        ContextCompat.getColor(requireContext(), R.color.blue2),
                        ContextCompat.getColor(requireContext(), R.color.blue3)
                    )

                    while (colors.size < it.data.size) {
                        colors.add(Color.LTGRAY)
                    }

                    binding.categoryProgressView.updateSections(
                        it.data.map { it.percent.toFloat() },
                        colors,
                        topCategories.map { it.category })
                }
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
        items.add(newCategory)

        // 어댑터에 새로운 항목 추가
        val currentList = adapter.currentList.toMutableList()
        currentList.add(
            currentList.size - 1,
            ConsumeCategoryAdapter.CategoryItem.Normal(newCategory, false, "0", "0")
        )
        adapter.submitList(currentList)

        // 삭제 버튼 가시성 업데이트
        updateDeleteButtonVisibility(items)
    }

    private fun deleteSelectedItems() {
        val deletedItems = adapter.deleteSelectedItems()
        items.removeAll { it in deletedItems }
        updateDeleteButtonVisibility(items)
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

    @SuppressLint("DefaultLocale")
    private fun changeMonth(currentMonth: String?, increment: Int): String {
        val yearMonth = currentMonth?.split("-") ?: return ""
        var year = yearMonth[0].toInt()
        var month = yearMonth[1].toInt()

        month += increment

        if (month > 12) {
            month = 1
            year += 1
        } else if (month < 1) {
            month = 12
            year -= 1
        }

        return String.format("%04d-%02d", year, month)
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
    }

    private fun navigateToAnalysisConsumeLocationFragment(category: String, amount: String) {
        val action = AnalysisConsumeFragmentDirections
            .actionAnalysisConsumeFragmentToAnalysisCategoryConsumeFragment(category, amount)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }
}
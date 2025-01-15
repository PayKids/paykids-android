package com.paykids.presentation.view.diary

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisCategoryAllowanceBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisCategoryConsumeFragment : BaseFragment<FragmentAnalysisCategoryAllowanceBinding>() {
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val viewModel: DiaryViewModel by activityViewModels()
    private lateinit var adapter: CategoryDetailAllowanceAdapter

    private var isConsumeClicked: Boolean = true

    override fun initView() {
        val args: AnalysisCategoryConsumeFragmentArgs by navArgs()
        val currentYear = args.currentYear
        val currentMonth = args.currentMonth
        val category = args.category
        val amount = args.amount
        isConsumeClicked = args.isConsumeClicked

        val formattedText = if (isConsumeClicked) {
            "${category}에서 ${Constants.formatAmount(amount)}원 소비 중"
        } else {
            "${category}에서 ${Constants.formatAmount(amount)}원 수입 중"
        }
        val spannableString = SpannableString(formattedText)

        val placeStartIndex = formattedText.indexOf(category)
        val placeEndIndex = placeStartIndex + category.length
        val blueColor = ContextCompat.getColor(requireContext(), R.color.blue1)

        spannableString.setSpan(
            ForegroundColorSpan(blueColor),
            placeStartIndex,
            placeEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvConsumeInfo.text = spannableString

        fetchData(currentYear, currentMonth, category)
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            val args: AnalysisCategoryConsumeFragmentArgs by navArgs()
            val currentYear = args.currentYear
            val currentMonth = args.currentMonth

            parentFragmentManager.setFragmentResult(
                "CATEGORY_BACK_RESULT",
                Bundle().apply {
                    putInt("year", currentYear)
                    putInt("month", currentMonth)
                }
            )
            parentFragmentManager.popBackStack()
        }
    }

    private fun fetchData(year: Int, month: Int, category: String) {
        if (isConsumeClicked) {
            viewModel.getMonthCategoryExpense(year, month, category)
        } else {
            viewModel.getMonthCategoryIncome(year, month, category)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.categoryExpenseState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("카테고리 별 월별 소비 금액 조회 성공: ${it.data}")
                    if (!::adapter.isInitialized) {
                        adapter = CategoryDetailAllowanceAdapter(isConsumeClicked, this)
                        binding.rvCategoryConsume.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvCategoryConsume.adapter = adapter
                    }

                    val formattedDetails = it.data.map {
                        val dateParts = it.date.substring(5)
                        val formattedDate = dateParts.replace("-", " / ")
                        Triple(formattedDate, it.amount, it.memo)
                    }
                    adapter.submitList(formattedDetails)
                }
            }
        }

        viewModel.categoryIncomeState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("카테고리 별 월별 수입 금액 조회 성공: ${it.data}")
                    if (!::adapter.isInitialized) {
                        adapter = CategoryDetailAllowanceAdapter(isConsumeClicked, this)
                        binding.rvCategoryConsume.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvCategoryConsume.adapter = adapter
                    }

                    val formattedDetails = it.data.map {
                        val dateParts = it.date.substring(5)
                        val formattedDate = dateParts.replace("-", " / ")
                        Triple(formattedDate, it.amount, it.memo)
                    }
                    adapter.submitList(formattedDetails)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
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
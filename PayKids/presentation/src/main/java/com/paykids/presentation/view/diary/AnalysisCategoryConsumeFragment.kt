package com.paykids.presentation.view.diary

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisCategoryConsumeBinding
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisCategoryConsumeFragment : BaseFragment<FragmentAnalysisCategoryConsumeBinding>() {
    private val viewModel: DiaryViewModel by activityViewModels()

    private lateinit var adapter: CategoryConsumeAdapter

    private var category: String = ""
    private var amount: String = ""

    override fun initView() {
        val args = AnalysisCategoryConsumeFragmentArgs.fromBundle(requireArguments())
        category = args.category
        amount = args.amount

        val amount = this.amount.toInt()
        val formattedText = "${category}에서 ${Constants.formatAmount(amount)}원 소비 중"
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

        fetchData()
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun fetchData() {
        val categoryDetails = viewModel.getConsumptionByCategory(category)

        if (!::adapter.isInitialized) {
            adapter = CategoryConsumeAdapter(this)
            binding.rvCategoryConsume.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCategoryConsume.adapter = adapter
        }
        val formattedDetails = categoryDetails.map {
            Triple(it.first, it.second, it.third)
        }
        adapter.submitList(formattedDetails)
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
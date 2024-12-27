package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentDiaryBinding
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

    override fun initView() {
        detailAdapter = DetailConsumeAdapter()
        binding.rvDetailConsume.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            this.adapter = detailAdapter
        }

        viewModel.selectedDateDetails.observe(viewLifecycleOwner) { details ->
            (binding.rvDetailConsume.adapter as DetailConsumeAdapter).submitList(details)
        }

        calendarAdapter = DiaryMonthCalendarStateAdapter(requireActivity())
        binding.vpCalendarMonth.adapter = calendarAdapter
        binding.vpCalendarMonth.setCurrentItem(Int.MAX_VALUE / 2, false)
        binding.vpCalendarMonth.offscreenPageLimit = 1

        // viewPager 스크롤 막기
        binding.vpCalendarMonth.getChildAt(0).setOnTouchListener { _, _ -> true }

        updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.ivConsumptionStatus.setOnClickListener {
            navController.navigate(R.id.analysisConsumeFragment)
        }

        binding.ibAddPocketMoney.setOnClickListener {
            val dialog = DiaryDialog(this)
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
            binding.vpCalendarMonth.setCurrentItem(currentPos - 1, false)
        }

        binding.ibRight.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
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
        val selectedDate = SimpleDateFormat("yyyy-MM", Locale.KOREAN).format(calendar.time)
        binding.tvMonth.text = "${calendar.get(Calendar.MONTH) + 1}월"

        viewModel.fetchDetailsForDate(selectedDate)
    }

}
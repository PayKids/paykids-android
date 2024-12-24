package com.paykids.presentation.view.diary

import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisConsumeLocationBinding

class AnalysisConsumeLocationFragment : BaseFragment<FragmentAnalysisConsumeLocationBinding>() {
    override fun initView() {
        val args = AnalysisConsumeLocationFragmentArgs.fromBundle(requireArguments())
        val place = args.consumptionPlace
        val amount = args.consumeAmount
        val formattedText = "${place}에서 ${amount}원 소비 중"
        binding.tvConsumeInfo.text = formattedText
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}
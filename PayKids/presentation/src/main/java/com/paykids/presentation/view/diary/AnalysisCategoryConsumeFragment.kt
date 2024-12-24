package com.paykids.presentation.view.diary

import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisCategoryConsumeBinding

class AnalysisCategoryConsumeFragment : BaseFragment<FragmentAnalysisCategoryConsumeBinding>() {
    override fun initView() {
        val args = AnalysisCategoryConsumeFragmentArgs.fromBundle(requireArguments())
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
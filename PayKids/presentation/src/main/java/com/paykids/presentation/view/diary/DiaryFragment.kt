package com.paykids.presentation.view.diary

import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.custom.DiaryDialog
import com.paykids.presentation.databinding.FragmentDiaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryFragment : BaseFragment<FragmentDiaryBinding>(), ConfirmDialogInterface {

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.ibAddPocketMoney.setOnClickListener {
            val dialog = DiaryDialog(this)
            dialog.isCancelable = true
            dialog.show(parentFragmentManager, "AddPocketMoneyDialog")
        }
    }

    override fun onYesButtonClick() {

    }

}
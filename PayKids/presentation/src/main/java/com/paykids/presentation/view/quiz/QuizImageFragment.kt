package com.paykids.presentation.view.quiz

import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizImageFragment : BaseFragment<FragmentQuizImageBinding>(), ConfirmDialogInterface {
    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            val dialog =
                ExitDialog(
                    this,
                    R.string.dialog_check_exit,
                )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "AllClearDialog")
        }
    }

    override fun onYesButtonClick() {

    }
}
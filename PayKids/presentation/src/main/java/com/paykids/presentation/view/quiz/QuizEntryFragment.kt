package com.paykids.presentation.view.quiz

import androidx.navigation.fragment.findNavController
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizEntryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizEntryFragment : BaseFragment<FragmentQuizEntryBinding>(), ConfirmDialogInterface {
    private val incorrectQuiz = 0
    private var clear = false

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.btnStudy.setOnClickListener {
            navController.navigate(R.id.studyFragment)
        }

        binding.btnQuiz.setOnClickListener { }

        binding.btnReview.setOnClickListener {
            if (!clear && incorrectQuiz == 0) {
                val dialog =
                    IncorrectDialog(
                        this,
                        R.string.dialog_incorrect_nothing,
                    )
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "IncorrectNothingDialog")
            } else if (clear && incorrectQuiz == 0) {
                val dialog =
                    IncorrectDialog(
                        this,
                        R.string.dialog_all_correct,
                    )
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "AllClearDialog")
            } else {
                // 문제를 한번이라도 풀고 틀린 문제가 하나라도 있는 경우 오답노트 페이지로 이동

            }
        }

    }

    override fun onYesButtonClick() {
        // 퀴즈 풀기 페이지로 이동
    }
}
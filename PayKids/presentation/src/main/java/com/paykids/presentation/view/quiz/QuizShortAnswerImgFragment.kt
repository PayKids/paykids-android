package com.paykids.presentation.view.quiz

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizShortAnswerImgBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizShortAnswerImgFragment : BaseFragment<FragmentQuizShortAnswerImgBinding>(), ConfirmDialogInterface {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizImageFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0

    override fun initView() {
        stageNumber = args.stageNumber
        quizNumber = args.quizNumber

        quizEntryViewModel.getQuiz(stageNumber, quizNumber)
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
        binding.tvDecision.setOnClickListener {
            // getQuiz를 호출하여 다음 퀴즈를 로드
            quizEntryViewModel.getQuiz(stageNumber, quizNumber + 1)  // quizNumber는 다음 퀴즈 번호로 증가시킴
        }
    }

    override fun setObserver() {
        super.setObserver()

        quizEntryViewModel.quizState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    val quiz = it.data
                    if (quiz.number > quizNumber ) { // 퀴즈를 풀어 다음 퀴즈 번호를 관찰한 경우
                        navigateToNextQuiz()
                        return@observe
                    }
                    LoggerUtils.d("Quiz loaded: ${quiz.question}")
                    binding.tvQuestion.text = quiz.question
                    binding.tvQuizProgress.text = "${quiz.number}/${quiz.count}"
                }
            }
        }
    }

    override fun onYesButtonClick() {

    }

    private fun navigateToNextQuiz() {
        val quizState = quizEntryViewModel.quizState.value
        if (quizState is UiState.Success) {
            val quiz = quizState.data
            val stageNumber = quiz.stage
            val quizNumber = quiz.number

            // 각 퀴즈 유형에 따라 프래그먼트로 전달
            when (quiz.quizType) {
                "IMAGE_CHOICE" -> {
                    val action = QuizShortAnswerImgFragmentDirections
                        .actionQuizShortAnswerImgFragmentToQuizImageFragment(stageNumber, quizNumber)
                    findNavController().navigate(action)
                }
                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizShortAnswerImgFragmentDirections
                            .actionQuizShortAnswerImgFragmentToQuizMultipleChoiceFragment(stageNumber, quizNumber)
                    } else {
                        QuizShortAnswerImgFragmentDirections
                            .actionQuizShortAnswerImgFragmentToQuizMultipleChoiceImgFragment(stageNumber, quizNumber)
                    }
                    findNavController().navigate(action)
                }
                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizShortAnswerImgFragmentDirections
                            .actionQuizShortAnswerImgFragmentToQuizShortAnswerFragment(stageNumber, quizNumber)
                    } else {
                        QuizShortAnswerImgFragmentDirections
                            .actionQuizShortAnswerImgFragmentToQuizShortAnswerImgFragment(stageNumber, quizNumber)
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }
}
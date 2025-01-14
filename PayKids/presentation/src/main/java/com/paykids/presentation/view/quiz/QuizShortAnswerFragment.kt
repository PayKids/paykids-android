package com.paykids.presentation.view.quiz

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizShortAnswerBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizShortAnswerFragment : BaseFragment<FragmentQuizShortAnswerBinding>(),
    ConfirmDialogInterface {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizShortAnswerFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0
    private var correctAnswer: String? = null
    private var userAnswer: String = ""


    override fun initView() {
        stageNumber = args.stageNumber
        quizNumber = args.quizNumber

        quizEntryViewModel.getQuiz(stageNumber, quizNumber)
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            val dialog = ExitDialog(
                this,
                R.string.dialog_check_exit,
            )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "AllClearDialog")
        }
        binding.tvDecision.setOnClickListener {
            // 사용자가 입력한 답을 가져와서 체크
            userAnswer = binding.etAnswer.text.toString().trim()
            // 정답이 비어 있는 경우 메시지 표시
            if (userAnswer.isEmpty()) {
                showToast("정답을 입력해주세요.")
                return@setOnClickListener
            }
            // 답안을 체크하는 메서드 호출
            quizEntryViewModel.checkAnswer(stageNumber, quizNumber, userAnswer)

            // 딜레이 후 다음 퀴즈 로드
            lifecycleScope.launch {
                delay(2000L) // 2초 딜레이

                // 마지막 퀴즈인 경우 QuizClearFragment로 이동
                val quizState = quizEntryViewModel.quizState.value
                if (quizState is UiState.Success) {
                    val quiz = quizState.data
                    if (quizNumber == quiz.count) {
                        val action =
                            QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizClearFragment(
                                stageNumber
                            )
                        findNavController().navigate(action)
                    }
                }

                quizEntryViewModel.getQuiz(stageNumber, quizNumber + 1)
            }
        }
    }

    @SuppressLint("SetTextI18n")
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
                    if (quiz.number > quizNumber) { // 퀴즈를 풀어 다음 퀴즈 번호를 관찰한 경우
                        navigateToNextQuiz()
                        return@observe
                    }
                    LoggerUtils.d("Quiz loaded: ${quiz.question}")
                    binding.tvQuestion.text = quiz.question
                    binding.tvQuizProgress.text = "${quiz.number}/${quiz.count}"
                    correctAnswer = quiz.answer
                }
            }
        }

        quizEntryViewModel.checkAnswerState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    // userAnswer가 비어 있으면 UI 업데이트를 하지 않음
                    if (userAnswer.isEmpty()) {
                        return@observe
                    }
                    updateUIForAnswer()
                }
            }
        }
    }

    override fun onYesButtonClick() {

    }

    private fun updateUIForAnswer() {
        // 사용자가 입력한 답과 정답 비교
        if (userAnswer.isNotEmpty()) {
            if (userAnswer == correctAnswer) {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_correct)
                binding.llCorrectAnswer.visibility = View.VISIBLE
            } else {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_wrong)
                binding.llWrongAnswer.visibility = View.VISIBLE
            }
        }
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
                    val action =
                        QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizImageFragment(
                            stageNumber,
                            quizNumber
                        )
                    findNavController().navigate(action)
                }

                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizMultipleChoiceFragment(
                            stageNumber, quizNumber
                        )
                    } else {
                        QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizMultipleChoiceImgFragment(
                            stageNumber, quizNumber
                        )
                    }
                    findNavController().navigate(action)
                }

                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizShortAnswerFragment(
                            stageNumber, quizNumber
                        )
                    } else {
                        QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizShortAnswerImgFragment(
                            stageNumber, quizNumber
                        )
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }

}
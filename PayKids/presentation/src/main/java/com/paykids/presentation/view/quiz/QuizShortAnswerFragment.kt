package com.paykids.presentation.view.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizShortAnswerBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizShortAnswerFragment : BaseFragment<FragmentQuizShortAnswerBinding>(),
    ConfirmDialogInterface {
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizShortAnswerFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0
    private var correctAnswer: String? = null
    private var userAnswer: String = ""
    private var incorrectQuizIndex: Int = -1
    private var incorrectQuizzes = mutableListOf<Int>()


    override fun initView() {
        stageNumber = args.stageNumber
        quizNumber = args.quizNumber
        incorrectQuizIndex = args.incorrectQuizIndex

        quizEntryViewModel.getIncorrectQuizNumbers(stageNumber)
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
            userAnswer = binding.etAnswer.text.toString().trim()
            if (userAnswer.isEmpty()) {
                showToast("정답을 입력해주세요.")
                return@setOnClickListener
            }
            quizEntryViewModel.checkAnswer(stageNumber, quizNumber, userAnswer)

            lifecycleScope.launch {
                delay(2000L)

                // 마지막 퀴즈인 경우 QuizClearFragment로 이동
                val quizState = quizEntryViewModel.quizState.value
                if (quizState is UiState.Success) {
                    val quiz = quizState.data
                    if (quizNumber == quiz.count) {
                        val action =
                            QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizClearFragment(
                                stageNumber
                            )
                        findNavController().navigate(action)
                    }
                }
                if (incorrectQuizIndex != -1) { // 오답 노트 풀기(오답 퀴즈 진행 중)일 경우
                    if (incorrectQuizIndex >= incorrectQuizzes.size) { // 마지막 오답 퀴즈인 경우
                        val action =
                            QuizShortAnswerFragmentDirections.actionQuizShortAnswerFragmentToQuizClearFragment(
                                stageNumber
                            )
                        findNavController().navigate(action)
                    } else {
                        quizEntryViewModel.getQuiz(
                            stageNumber,
                            incorrectQuizzes[incorrectQuizIndex]
                        )
                    }

                } else { // 다음 퀴즈 조회
                    quizEntryViewModel.getQuiz(stageNumber, quizNumber + 1)
                }
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
                        if (incorrectQuizIndex > 0) {
                            navigateToIncorrectQuiz(incorrectQuizIndex)
                            return@observe
                        }
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

        quizEntryViewModel.incorrectQuizState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    incorrectQuizzes = it.data.toMutableList()
                }
            }
        }
    }

    override fun onYesButtonClick() {

    }

    override fun onQuizEntryButtonClick(stageNumber: Int) {

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

    private fun navigateToIncorrectQuiz(index: Int) {
        val incorrectQuizNumber = incorrectQuizzes[index]

        val quizState = quizEntryViewModel.quizState.value
        if (quizState is UiState.Success) {
            val quiz = quizState.data
            val action = when (quiz.quizType) {
                "IMAGE_CHOICE" -> QuizShortAnswerFragmentDirections
                    .actionQuizShortAnswerFragmentToQuizImageFragment(
                        stageNumber,
                        incorrectQuizNumber,
                        index + 1
                    )

                "TEXT_CHOICE" -> if (quiz.imageURL.isNullOrEmpty()) {
                    QuizShortAnswerFragmentDirections
                        .actionQuizShortAnswerFragmentToQuizMultipleChoiceFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                } else {
                    QuizShortAnswerFragmentDirections
                        .actionQuizShortAnswerFragmentToQuizMultipleChoiceImgFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                }

                "SHORT_ANSWER" -> if (quiz.imageURL.isNullOrEmpty()) {
                    QuizShortAnswerFragmentDirections
                        .actionQuizShortAnswerFragmentToQuizShortAnswerFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                } else {
                    QuizShortAnswerFragmentDirections
                        .actionQuizShortAnswerFragmentToQuizShortAnswerImgFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                }

                else -> null
            }

            action?.let { findNavController().navigate(it) }
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
package com.paykids.presentation.view.quiz

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizImageBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizImageFragment : BaseFragment<FragmentQuizImageBinding>(), ConfirmDialogInterface {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizImageFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0
    private var isCorrect: Boolean? = null
    private var correctAnswer: String? = null
    private var selectedAnswer: Int = 0
    private var userAnswer: String = ""

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

        val answerFirst = binding.root.findViewById<View>(R.id.answer_first)
        val answerSecond = binding.root.findViewById<View>(R.id.answer_second)
        val answerThird = binding.root.findViewById<View>(R.id.answer_third)
        val answerFourth = binding.root.findViewById<View>(R.id.answer_fourth)
        answerFirst.setOnClickListener { onAnswerClicked(1) }
        answerSecond.setOnClickListener { onAnswerClicked(2) }
        answerThird.setOnClickListener { onAnswerClicked(3) }
        answerFourth.setOnClickListener { onAnswerClicked(4) }
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
                    if (quiz.number > quizNumber) { // 퀴즈를 풀어 다음 퀴즈 번호를 관찰한 경우
                        navigateToNextQuiz()
                        return@observe
                    }
                    LoggerUtils.d("Quiz loaded: ${quiz.question}")
                    binding.tvQuestion.text = quiz.question
                    binding.tvQuizProgress.text = "${quiz.number}/${quiz.count}"
                    quiz.imageURL?.let { it1 -> loadChoiceImages(it1) }
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
                    isCorrect = it.data
                    updateUIForAnswer()
                }
            }
        }
    }

    override fun onYesButtonClick() {

    }

    // 이미지 로딩 함수
    private fun loadChoiceImages(imageURLMap: Map<String, String>) {
        // Glide로 이미지 로딩
        Glide.with(this)
            .load(imageURLMap["image1"])
            .into(binding.answerFirst.ivAnswerImage)

        Glide.with(this)
            .load(imageURLMap["image2"])
            .into(binding.answerSecond.ivAnswerImage)

        Glide.with(this)
            .load(imageURLMap["image3"])
            .into(binding.answerThird.ivAnswerImage)

        Glide.with(this)
            .load(imageURLMap["image4"])
            .into(binding.answerFourth.ivAnswerImage)

    }

    // 답을 클릭한 경우 호출되는 함수
    private fun onAnswerClicked(answerNumber: Int) {
        selectedAnswer = answerNumber
        val userAnswer = when (answerNumber) {
            1 -> "A"
            2 -> "B"
            3 -> "C"
            4 -> "D"
            else -> ""
        }
        this.userAnswer = userAnswer
        // 정답 확인 메서드 호출
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

    private fun updateUIForAnswer() {
        val selectedAnswerView = when (selectedAnswer) {
            1 -> binding.answerFirst.root
            2 -> binding.answerSecond.root
            3 -> binding.answerThird.root
            4 -> binding.answerFourth.root
            else -> null
        }

        // 정답 여부에 따른 UI 변경
        if (selectedAnswerView != null) {
            // 정답일 때
            if (isCorrect == true) {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_correct)
                selectedAnswerView.setBackgroundResource(R.drawable.shape_quiz_box_blue)
                binding.llCorrectAnswer.visibility = View.VISIBLE
            } else if (isCorrect == false) {
                // 오답일 때
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_wrong)
                selectedAnswerView.setBackgroundResource(R.drawable.shape_quiz_box_red)

                // 정답에 해당하는 선택지의 배경 색 변경
                val correctAnswerView = getCorrectAnswerView()
                correctAnswerView?.setBackgroundResource(R.drawable.shape_quiz_box_blue)
                binding.llWrongAnswer.visibility = View.VISIBLE
            }
        }
    }

    private fun getCorrectAnswerView(): View? {
        // 정답에 해당하는 뷰 반환
        return when (correctAnswer) {
            "A" -> binding.answerFirst.root
            "B" -> binding.answerSecond.root
            "C" -> binding.answerThird.root
            "D" -> binding.answerFourth.root
            else -> null
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
                    val action = QuizImageFragmentDirections
                        .actionQuizImageFragmentToQuizImageFragment(stageNumber, quizNumber)
                    findNavController().navigate(action)
                }

                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizImageFragmentDirections
                            .actionQuizImageFragmentToQuizMultipleChoiceFragment(
                                stageNumber,
                                quizNumber
                            )
                    } else {
                        QuizImageFragmentDirections
                            .actionQuizImageFragmentToQuizMultipleChoiceImgFragment(
                                stageNumber,
                                quizNumber
                            )
                    }
                    findNavController().navigate(action)
                }

                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizImageFragmentDirections
                            .actionQuizImageFragmentToQuizShortAnswerFragment(
                                stageNumber,
                                quizNumber
                            )
                    } else {
                        QuizImageFragmentDirections
                            .actionQuizImageFragmentToQuizShortAnswerImgFragment(
                                stageNumber,
                                quizNumber
                            )
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }

}
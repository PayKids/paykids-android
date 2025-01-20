package com.paykids.presentation.view.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizImageFragment : BaseFragment<FragmentQuizImageBinding>(), ConfirmDialogInterface {
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizImageFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0
    private var isCorrect: Boolean? = null
    private var correctAnswer: String? = null
    private var selectedAnswer: Int = 0
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
            val dialog =
                ExitDialog(
                    this,
                    R.string.dialog_check_exit,
                )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "QuizExitDialog")
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
                        if (incorrectQuizIndex > 0) { // 다음 오답 퀴즈의 인덱스가 0보다 클 때는 오답 퀴즈 전용 메서드 호출
                            navigateToIncorrectQuiz(incorrectQuizIndex)
                            return@observe
                        }
                        navigateToNextQuiz()
                        return@observe
                    }
                    binding.tvQuestion.text = quiz.question
                    binding.tvQuizProgress.text = "${quiz.number}/${quiz.count}"
                    quiz.imageURL?.let { it1 -> loadChoiceImages(it1) }
                    quiz.choices?.let { it2 -> loadChoiceTexts(it2) }
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

    private fun loadChoiceImages(imageURLMap: Map<String, String>) {
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

    private fun loadChoiceTexts(textChoices: Map<String, String>) {
        binding.answerFirst.tvAnswerText.text = textChoices["A"]
        binding.answerSecond.tvAnswerText.text = textChoices["B"]
        binding.answerThird.tvAnswerText.text = textChoices["C"]
        binding.answerFourth.tvAnswerText.text = textChoices["D"]
    }

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
        quizEntryViewModel.checkAnswer(stageNumber, quizNumber, userAnswer)

        lifecycleScope.launch {
            delay(2000L)

            // 마지막 퀴즈인 경우 QuizClearFragment로 이동
            val quizState = quizEntryViewModel.quizState.value
            if (quizState is UiState.Success) {
                val quiz = quizState.data
                if (quizNumber == quiz.count) {
                    val action =
                        QuizImageFragmentDirections.actionQuizImageFragmentToQuizClearFragment(
                            stageNumber
                        )
                    findNavController().navigate(action)
                }
            }
            if (incorrectQuizIndex != -1) { // 오답 노트 풀기(오답 퀴즈 진행 중)일 경우 다음 오답 퀴즈 조회
                if (incorrectQuizIndex >= incorrectQuizzes.size) { // 마지막 오답 퀴즈인 경우
                    val action =
                        QuizImageFragmentDirections.actionQuizImageFragmentToQuizClearFragment(
                            stageNumber
                        )
                    findNavController().navigate(action)
                } else {
                    quizEntryViewModel.getQuiz(stageNumber, incorrectQuizzes[incorrectQuizIndex])
                }
            } else { // 다음 퀴즈 조회
                quizEntryViewModel.getQuiz(stageNumber, quizNumber + 1)
            }
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

        if (selectedAnswerView != null) {
            if (isCorrect == true) {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_correct)
                selectedAnswerView.setBackgroundResource(R.drawable.shape_quiz_box_blue)
                binding.llCorrectAnswer.visibility = View.VISIBLE
            } else if (isCorrect == false) {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_wrong)
                selectedAnswerView.setBackgroundResource(R.drawable.shape_quiz_box_red)

                val correctAnswerView = getCorrectAnswerView()
                correctAnswerView?.setBackgroundResource(R.drawable.shape_quiz_box_blue)
                binding.llWrongAnswer.visibility = View.VISIBLE
            }
        }
    }

    private fun getCorrectAnswerView(): View? {
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

    private fun navigateToIncorrectQuiz(index: Int) {
        val incorrectQuizNumber = incorrectQuizzes[incorrectQuizIndex]

        val quizState = quizEntryViewModel.quizState.value
        if (quizState is UiState.Success) {
            val quiz = quizState.data
            val action = when (quiz.quizType) {
                "IMAGE_CHOICE" -> QuizImageFragmentDirections
                    .actionQuizImageFragmentToQuizImageFragment(
                        stageNumber,
                        incorrectQuizNumber,
                        index + 1
                    )

                "TEXT_CHOICE" -> if (quiz.imageURL.isNullOrEmpty()) {
                    QuizImageFragmentDirections
                        .actionQuizImageFragmentToQuizMultipleChoiceFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                } else {
                    QuizImageFragmentDirections
                        .actionQuizImageFragmentToQuizMultipleChoiceImgFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                }

                "SHORT_ANSWER" -> if (quiz.imageURL.isNullOrEmpty()) {
                    QuizImageFragmentDirections
                        .actionQuizImageFragmentToQuizShortAnswerFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index + 1
                        )
                } else {
                    QuizImageFragmentDirections
                        .actionQuizImageFragmentToQuizShortAnswerImgFragment(
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
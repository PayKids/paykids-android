package com.paykids.presentation.view.quiz

import QuizMultipleChoiceRvAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizMultipleChoiceBinding
import com.paykids.presentation.utils.QuizSoundManager
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizMultipleChoiceFragment : BaseFragment<FragmentQuizMultipleChoiceBinding>(),
    ConfirmDialogInterface {
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizMultipleChoiceFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0
    private var isCorrect: Boolean? = null
    private var correctAnswerLetter: String? = null
    private var userAnswer: String = ""
    private var incorrectQuizIndex: Int = -1
    private var incorrectQuizzes = mutableListOf<Int>()

    private val adapter by lazy {
        QuizMultipleChoiceRvAdapter { answer -> onAnswerClicked(answer) }
    }

    override fun initView() {
        stageNumber = args.stageNumber
        quizNumber = args.quizNumber
        incorrectQuizIndex = args.incorrectQuizIndex
        binding.rvAnswers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@QuizMultipleChoiceFragment.adapter
        }

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
    }

    @SuppressLint("SetTextI18n")
    override fun setObserver() {
        super.setObserver()

        quizEntryViewModel.quizState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {
                }

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

                    val answers = quiz.choices?.map { entry ->
                        entry.key to entry.value
                    } ?: emptyList()
                    adapter.submitList(answers)
                    correctAnswerLetter = quiz.answer
                    adapter.setCorrectAnswer(correctAnswerLetter!!)
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
                    playEffect()
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
        TODO("Not yet implemented")
    }

    private fun onAnswerClicked(answerLetter: String) {
        userAnswer = answerLetter
        quizEntryViewModel.checkAnswer(stageNumber, quizNumber, answerLetter)
        adapter.updateSelectedAnswer(answerLetter)

        lifecycleScope.launch {
            delay(2000L)

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
                    return@launch
                }
            }

            if (incorrectQuizIndex != -1) { // 오답 노트 풀기(오답 퀴즈 진행 중)일 경우
                if (incorrectQuizIndex >= incorrectQuizzes.size) { // 마지막 오답 퀴즈인 경우
                    val action =
                        QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizClearFragment(
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
        if (isCorrect == true) {
            binding.ivBackground.setImageResource(R.drawable.bg_quiz_correct)
            binding.llCorrectAnswer.visibility = View.VISIBLE
        } else if (isCorrect == false) {
            binding.ivBackground.setImageResource(R.drawable.bg_quiz_wrong)
            binding.llWrongAnswer.visibility = View.VISIBLE
        }
    }

    private fun playEffect() {
        if (isCorrect == true) {
            QuizSoundManager.playEffect("correct")
        } else {
            QuizSoundManager.playEffect("wrong")
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
                    val action =
                        QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizImageFragment(
                            stageNumber, quizNumber
                        )
                    findNavController().navigate(action)
                }

                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizMultipleChoiceFragment(
                            stageNumber, quizNumber
                        )
                    } else {
                        QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizMultipleChoiceImgFragment(
                            stageNumber, quizNumber
                        )
                    }
                    findNavController().navigate(action)
                }

                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizShortAnswerFragment(
                            stageNumber, quizNumber
                        )
                    } else {
                        QuizMultipleChoiceFragmentDirections.actionQuizMultipleChoiceFragmentToQuizShortAnswerImgFragment(
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
                "IMAGE_CHOICE" -> QuizMultipleChoiceFragmentDirections
                    .actionQuizMultipleChoiceFragmentToQuizImageFragment(
                        stageNumber,
                        incorrectQuizNumber,
                        index+1
                    )

                "TEXT_CHOICE" -> if (quiz.imageURL.isNullOrEmpty()) {
                    QuizMultipleChoiceFragmentDirections
                        .actionQuizMultipleChoiceFragmentToQuizMultipleChoiceFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index+1
                        )
                } else {
                    QuizMultipleChoiceFragmentDirections
                        .actionQuizMultipleChoiceFragmentToQuizMultipleChoiceImgFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index+1
                        )
                }

                "SHORT_ANSWER" -> if (quiz.imageURL.isNullOrEmpty()) {
                    QuizMultipleChoiceFragmentDirections
                        .actionQuizMultipleChoiceFragmentToQuizShortAnswerFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index+1
                        )
                } else {
                    QuizMultipleChoiceFragmentDirections
                        .actionQuizMultipleChoiceFragmentToQuizShortAnswerImgFragment(
                            stageNumber,
                            incorrectQuizNumber,
                            index+1
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
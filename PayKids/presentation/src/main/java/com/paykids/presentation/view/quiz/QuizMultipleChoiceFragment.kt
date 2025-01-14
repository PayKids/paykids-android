package com.paykids.presentation.view.quiz

import QuizMultipleChoiceRvAdapter
import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizMultipleChoiceBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizMultipleChoiceFragment : BaseFragment<FragmentQuizMultipleChoiceBinding>(),
    ConfirmDialogInterface {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizMultipleChoiceFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0
    private var isCorrect: Boolean? = null
    private var correctAnswerLetter: String? = null
    private var userAnswer: String = ""

    private val adapter by lazy {
        QuizMultipleChoiceRvAdapter { answer -> onAnswerClicked(answer) }
    }

    override fun initView() {
        stageNumber = args.stageNumber
        quizNumber = args.quizNumber
        // RecyclerView 초기화
        binding.rvAnswers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            // 어댑터 초기화
            adapter = this@QuizMultipleChoiceFragment.adapter
        }

        // 퀴즈 데이터 로드
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
                    // 로딩 상태 처리
                }

                is UiState.Success -> {
                    val quiz = it.data
                    if (quiz.number > quizNumber) { // 퀴즈를 풀어 다음 퀴즈 번호를 관찰한 경우
                        navigateToNextQuiz()
                        return@observe
                    }
                    LoggerUtils.d("Quiz loaded: ${quiz.question}")
                    binding.tvQuestion.text = quiz.question
                    binding.tvQuizProgress.text = "${quiz.number}/${quiz.count}"

                    // 답변 리스트 어댑터에 설정
                    val answers = quiz.choices?.map { entry ->
                        entry.key to entry.value // "A" to "Answer 1"
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
                }
            }
        }
    }

    override fun onYesButtonClick() {

    }

    private fun onAnswerClicked(answerLetter: String) {
        // 답변 클릭 시 처리
        LoggerUtils.d("Answer clicked: $answerLetter")

        userAnswer = answerLetter
        quizEntryViewModel.checkAnswer(stageNumber, quizNumber, answerLetter)
        adapter.updateSelectedAnswer(answerLetter)

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
        if (isCorrect == true) {
            binding.ivBackground.setImageResource(R.drawable.bg_quiz_correct)
            binding.llCorrectAnswer.visibility = View.VISIBLE
        } else if (isCorrect == false) {
            // 오답일 때
            binding.ivBackground.setImageResource(R.drawable.bg_quiz_wrong)
            binding.llWrongAnswer.visibility = View.VISIBLE
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
}
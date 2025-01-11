package com.paykids.presentation.view.quiz

import QuizMultipleChoiceRvAdapter
import androidx.fragment.app.activityViewModels
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

@AndroidEntryPoint
class QuizMultipleChoiceFragment : BaseFragment<FragmentQuizMultipleChoiceBinding>(),
    ConfirmDialogInterface {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizImageFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var quizNumber: Int = 0

    private val adapter by lazy {
        QuizMultipleChoiceRvAdapter { answer -> onAnswerClicked(answer) }
    }

    override fun initView() {
        stageNumber = args.stageNumber
        quizNumber = args.quizNumber
        // RecyclerView 초기화
        binding.rvAnswers.apply {
            layoutManager = LinearLayoutManager(requireContext()) // 또는 GridLayoutManager(requireContext(), spanCount)
            // 어댑터 초기화
            adapter = this@QuizMultipleChoiceFragment.adapter
        }

        // 퀴즈 데이터 로드
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
    }

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
                    if (quiz.number > quizNumber ) { // 퀴즈를 풀어 다음 퀴즈 번호를 관찰한 경우
                        navigateToNextQuiz()
                        return@observe
                    }
                    LoggerUtils.d("Quiz loaded: ${quiz.question}")
                    binding.tvQuestion.text = quiz.question
                    binding.tvQuizProgress.text = "${quiz.number}/${quiz.count}"

                    // 답변 리스트 어댑터에 설정
                    val answers = quiz.choices?.values?.toList()
                    adapter.submitList(answers)
                }
            }
        }
    }

    private fun onAnswerClicked(answer: String) {
        // 답변 클릭 시 처리
        LoggerUtils.d("Answer clicked: $answer / ${stageNumber} / ${quizNumber}")

        // 다음 퀴즈 로드
        quizEntryViewModel.getQuiz(stageNumber, quizNumber + 1)
        navigateToNextQuiz()
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
                    val action = QuizMultipleChoiceFragmentDirections
                        .actionQuizMultipleChoiceFragmentToQuizImageFragment(stageNumber, quizNumber)
                    findNavController().navigate(action)
                }
                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizMultipleChoiceFragmentDirections
                            .actionQuizMultipleChoiceFragmentToQuizMultipleChoiceFragment(stageNumber, quizNumber)
                    } else {
                        QuizMultipleChoiceFragmentDirections
                            .actionQuizMultipleChoiceFragmentToQuizMultipleChoiceImgFragment(stageNumber, quizNumber)
                    }
                    findNavController().navigate(action)
                }
                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizMultipleChoiceFragmentDirections
                            .actionQuizMultipleChoiceFragmentToQuizShortAnswerFragment(stageNumber, quizNumber)
                    } else {
                        QuizMultipleChoiceFragmentDirections
                            .actionQuizMultipleChoiceFragmentToQuizShortAnswerImgFragment(stageNumber, quizNumber)
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }
}
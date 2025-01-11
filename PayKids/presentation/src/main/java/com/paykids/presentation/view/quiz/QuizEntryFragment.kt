package com.paykids.presentation.view.quiz

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentQuizEntryBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuizEntryFragment : BaseFragment<FragmentQuizEntryBinding>(), ConfirmDialogInterface {
    private val quizEntryViewModel: QuizEntryViewModel by viewModels()
    private val args: QuizEntryFragmentArgs by navArgs()
    private val incorrectQuiz = 0
    private var clear = false

    override fun initView() {
        // 전달받은 스테이지 번호와 이름 사용
        val stageNumber = args.stageNumber
        val stageName = args.stageName

        binding.tvStage.text = "스테이지 $stageNumber"
        binding.tvStageName.text = stageName

        quizEntryViewModel.getQuiz(stageNumber, 1)
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnStudy.setOnClickListener {
            navController.navigate(R.id.studyFragment)
        }

        binding.btnQuiz.setOnClickListener {
            navigateToNextQuiz()
        }

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

    override fun setObserver() {
        super.setObserver()

        quizEntryViewModel.quizState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {

                }
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
                    val action = QuizEntryFragmentDirections
                        .actionQuizEntryFragmentToQuizImageFragment(stageNumber, quizNumber)
                    findNavController().navigate(action)
                }
                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizMultipleChoiceFragment(stageNumber, quizNumber)
                    } else {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizMultipleChoiceImgFragment(stageNumber, quizNumber)
                    }
                    findNavController().navigate(action)
                }
                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizShortAnswerFragment(stageNumber, quizNumber)
                    } else {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizShortAnswerImgFragment(stageNumber, quizNumber)
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }




    override fun onYesButtonClick() {
        // 퀴즈 풀기 페이지로 이동
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
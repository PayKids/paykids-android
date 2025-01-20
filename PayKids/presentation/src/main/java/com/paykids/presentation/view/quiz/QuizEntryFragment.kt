package com.paykids.presentation.view.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
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
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val quizEntryViewModel: QuizEntryViewModel by viewModels()
    private val args: QuizEntryFragmentArgs by navArgs()
    private var incorrectQuizzes = mutableListOf<Int>()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val stageNumber = args.stageNumber
        val stageName = args.stageName

        binding.tvStage.text = "스테이지 $stageNumber"
        binding.tvStageName.text = stageName

        quizEntryViewModel.getQuiz(stageNumber, 1)
        quizEntryViewModel.getIncorrectQuizNumbers(stageNumber)
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnStudy.setOnClickListener {
            val stageNumber = args.stageNumber
            val action = QuizEntryFragmentDirections
                .actionQuizEntryFragmentToStudyFragment(stageNumber)
            navController.navigate(action)
        }

        binding.btnQuiz.setOnClickListener {
            navigateToNextQuiz()
        }

        binding.btnReview.setOnClickListener {
            if (incorrectQuizzes.isNotEmpty()) {
                // 첫 번째 오답 문제 불러오기
                navigateToIncorrectQuiz()
            } else {
                val dialog = IncorrectDialog(this, R.string.dialog_incorrect_nothing, args.stageNumber)
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "IncorrectNothingDialog")
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

    private fun navigateToNextQuiz() {
        val quizState = quizEntryViewModel.quizState.value
        if (quizState is UiState.Success) {
            val quiz = quizState.data
            val stageNumber = quiz.stage
            val quizNumber = quiz.number

            when (quiz.quizType) {
                "IMAGE_CHOICE" -> {
                    val action = QuizEntryFragmentDirections
                        .actionQuizEntryFragmentToQuizImageFragment(stageNumber, quizNumber)
                    findNavController().navigate(action)
                }

                "TEXT_CHOICE" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizMultipleChoiceFragment(
                                stageNumber,
                                quizNumber
                            )
                    } else {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizMultipleChoiceImgFragment(
                                stageNumber,
                                quizNumber
                            )
                    }
                    findNavController().navigate(action)
                }

                "SHORT_ANSWER" -> {
                    val action = if (quiz.imageURL.isNullOrEmpty()) {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizShortAnswerFragment(
                                stageNumber,
                                quizNumber
                            )
                    } else {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizShortAnswerImgFragment(
                                stageNumber,
                                quizNumber
                            )
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun navigateToIncorrectQuiz() {
        if (incorrectQuizzes.isEmpty()) {
            // 모든 오답을 푼 경우, 완료 다이얼로그 표시
            val dialog = IncorrectDialog(this, R.string.dialog_all_correct, args.stageNumber)
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "ReviewCompleteDialog")
            return
        }

        val quizNumber = incorrectQuizzes[0]
        val stageNumber = args.stageNumber

        quizEntryViewModel.getQuiz(stageNumber, quizNumber) // 오답 퀴즈 가져오기

        quizEntryViewModel.quizState.observe(viewLifecycleOwner) { quizState ->
            if (quizState is UiState.Success) {
                val quiz = quizState.data
                val action = when (quiz.quizType) {
                    "IMAGE_CHOICE" -> QuizEntryFragmentDirections
                        .actionQuizEntryFragmentToQuizImageFragment(stageNumber, quizNumber, 1)
                    "TEXT_CHOICE" -> if (quiz.imageURL.isNullOrEmpty()) {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizMultipleChoiceFragment(stageNumber, quizNumber, 1)
                    } else {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizMultipleChoiceImgFragment(stageNumber, quizNumber, 1)
                    }
                    "SHORT_ANSWER" -> if (quiz.imageURL.isNullOrEmpty()) {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizShortAnswerFragment(stageNumber, quizNumber, 1)
                    } else {
                        QuizEntryFragmentDirections
                            .actionQuizEntryFragmentToQuizShortAnswerImgFragment(stageNumber, quizNumber, 1)
                    }
                    else -> null
                }

                action?.let { findNavController().navigate(it) }
            }
        }
    }


    override fun onYesButtonClick() {
        // 퀴즈 풀기 페이지로 이동
    }

    override fun onQuizEntryButtonClick(stageNumber: Int) {
        val action = QuizEntryFragmentDirections
            .actionQuizEntryFragmentToQuizImageFragment(stageNumber, 1) // 1번 문제로 이동
        findNavController().navigate(action)
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
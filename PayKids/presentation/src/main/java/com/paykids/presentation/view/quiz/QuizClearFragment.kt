package com.paykids.presentation.view.quiz

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paykids.domain.model.quiz.QuizClear
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentQuizClearBinding
import com.paykids.presentation.utils.QuizBgmManager
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizClearFragment : BaseFragment<FragmentQuizClearBinding>() {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizClearFragmentArgs by navArgs()
    private var stageNumber: Int = 0
    private var incorrectQuizzes = mutableListOf<Int>()

    override fun initView() {
        stageNumber = args.stageNumber
        quizEntryViewModel.checkClear(stageNumber)
        quizEntryViewModel.getIncorrectQuizNumbers(stageNumber)
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()

        binding.tvExit.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
    }

    override fun setObserver() {
        super.setObserver()

        quizEntryViewModel.checkClearState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    updateClearPage(it.data)
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

    private fun updateClearPage(response: QuizClear) {
        binding.tvClearMessage.text = getClearMessage(response.message, response.isCleared)
        updateClearUI(response.message, response.isCleared)
    }

    private fun getClearMessage(message: String, isCleared: Boolean): String {
        return when {
            message == "All Clear" && isCleared -> getString(R.string.text_box_all_clear)
            message == "First" && isCleared -> getString(R.string.text_box_first_clear)
            message == "First" && !isCleared -> getString(R.string.text_box_failed)
            message == "오답 노트" && isCleared -> getString(R.string.text_box_incorrect_answer_note_clear)
            message == "오답 노트" && !isCleared -> getString(R.string.text_box_failed)
            message == "복습" && isCleared -> getString(R.string.text_box_review)
            else -> getString(R.string.text_box_error)
        }
    }

    private fun updateClearUI(message: String, isCleared: Boolean) {
        val (backgroundRes, textBoxRes) = when { // 배경 업데이트
            (message == "First" && !isCleared) || (message == "오답 노트" && !isCleared) || (message == "복습" && !isCleared) ->
                R.drawable.bg_quiz_fail to R.drawable.shape_quiz_failed_box
            else -> R.drawable.bg_quiz_clear to R.drawable.shape_quiz_clear_box
        }

        binding.ivBackground.setImageResource(backgroundRes)
        binding.tvClearMessage.setBackgroundResource(textBoxRes)

        if (message == "First") { // 첫 스테이지 완료 시 오답 노트 풀기 버튼 활성화
            binding.tvWrongAnswerNote.visibility = View.VISIBLE
            binding.tvWrongAnswerNote.setOnClickListener { navigateToIncorrectQuiz() }
        }
    }

    private fun navigateToIncorrectQuiz() {
        val quizNumber = incorrectQuizzes[0]
        val stageNumber = args.stageNumber

        quizEntryViewModel.getQuiz(stageNumber, quizNumber) // 오답 퀴즈 가져오기

        quizEntryViewModel.quizState.observe(viewLifecycleOwner) { quizState ->
            if (quizState is UiState.Success) {
                val quiz = quizState.data
                val action = when (quiz.quizType) {
                    "IMAGE_CHOICE" -> QuizClearFragmentDirections
                        .actionQuizClearFragmentToQuizImageFragment(stageNumber, quizNumber, 1)
                    "TEXT_CHOICE" -> if (quiz.imageURL.isNullOrEmpty()) {
                        QuizClearFragmentDirections
                            .actionQuizClearFragmentToQuizMultipleChoiceFragment(stageNumber, quizNumber, 1)
                    } else {
                        QuizClearFragmentDirections
                            .actionQuizClearFragmentToQuizMultipleChoiceImgFragment(stageNumber, quizNumber, 1)
                    }
                    "SHORT_ANSWER" -> if (quiz.imageURL.isNullOrEmpty()) {
                        QuizClearFragmentDirections
                            .actionQuizClearFragmentToQuizShortAnswerFragment(stageNumber, quizNumber, 1)
                    } else {
                        QuizClearFragmentDirections
                            .actionQuizClearFragmentToQuizShortAnswerImgFragment(stageNumber, quizNumber, 1)
                    }
                    else -> null
                }

                action?.let { findNavController().navigate(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)

        QuizBgmManager.stopBgm()
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }
}
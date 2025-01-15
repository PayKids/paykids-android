package com.paykids.presentation.view.quiz

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paykids.domain.model.quiz.QuizClear
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentQuizClearBinding
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizClearFragment : BaseFragment<FragmentQuizClearBinding>() {
    private val quizEntryViewModel: QuizEntryViewModel by activityViewModels()
    private val args: QuizClearFragmentArgs by navArgs()
    private var stageNumber: Int = 0

    override fun initView() {
        stageNumber = args.stageNumber
        quizEntryViewModel.checkClear(stageNumber)
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
                    updateClearMessage(it.data)
                }
            }
        }
    }

    private fun updateClearMessage(response: QuizClear) {
        val message = response.message
        val isCleared = response.isCleared

        binding.tvClearMessage.text = when {
            message == "All Clear" && isCleared -> getString(R.string.text_box_all_clear)
            message == "First" && isCleared -> getString(R.string.text_box_first_clear)
            message == "First" && !isCleared -> getString(R.string.text_box_failed)
            message == "오답 노트" && isCleared -> getString(R.string.text_box_incorrect_answer_note_clear)
            message == "오답 노트" && !isCleared -> getString(R.string.text_box_failed)
            message == "복습" && isCleared -> getString(R.string.text_box_review)
            else -> getString(R.string.text_box_error)
        }

        // 배경 업데이트
        when {
            (message == "First" && !isCleared) || (message == "오답 노트" && !isCleared) || (message == "복습" && !isCleared) -> {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_fail)
                binding.tvClearMessage.setBackgroundResource(R.drawable.shape_quiz_failed_box)
            }

            else -> {
                binding.ivBackground.setImageResource(R.drawable.bg_quiz_clear)
                binding.tvClearMessage.setBackgroundResource(R.drawable.shape_quiz_clear_box)
            }
        }
    }
}
package com.paykids.presentation.view.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.quiz.Quiz
import com.paykids.domain.usecase.quiz.GetQuizUseCase
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizEntryViewModel @Inject constructor(
    private val getQuizUseCase: GetQuizUseCase
) : ViewModel() {
    private val _quizState = MutableLiveData<UiState<Quiz>>(UiState.Loading)
    val quizState: LiveData<UiState<Quiz>> get() = _quizState

    fun getQuiz(stage: Int, number: Int) {
        _quizState.value = UiState.Loading

        viewModelScope.launch {
            getQuizUseCase.invoke(stage, number)
                .onSuccess {
                    _quizState.value =
                        UiState.Success(
                            Quiz(
                                it.answer,
                                it.choices,
                                it.count,
                                it.id,
                                it.imageURL,
                                it.number,
                                it.question,
                                it.quizType,
                                it.stage
                            )
                        )
                }
                .onFailure {
                    _quizState.value = UiState.Failure(message = "퀴즈 불러오기 실패")
                }
        }
    }

}
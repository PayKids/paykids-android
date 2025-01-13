package com.paykids.presentation.view.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.quiz.Quiz
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.quiz.CheckAnswerUseCase
import com.paykids.domain.usecase.quiz.GetQuizUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizEntryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getQuizUseCase: GetQuizUseCase,
    private val checkAnswerUseCase: CheckAnswerUseCase
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

    private val _checkAnswerState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val checkAnswerState: LiveData<UiState<Boolean>> get() = _checkAnswerState

    fun checkAnswer(stage: Int, number: Int, answer: String) {
        _checkAnswerState.value = UiState.Loading

        viewModelScope.launch {
//            checkAnswerUseCase.invoke(
//                "eyJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiNjRjMjk5OTktMmIxYS00NDUwLWEzNWMtMzNiN2NmZTBiZTlhIiwiZW1haWwiOiJwYXlraWRzMjAyNEBnbWFpbC5jb20iLCJpYXQiOjE3MzY3NzUzNDksImV4cCI6MTczNjgxODU0OX0.rUYfMx5s7FjxbzOdMdm_ZpRiLPlj3e8uqvsyWI-d7YY",
//                stage,
//                number,
//                answer
//            )
            checkAnswerUseCase.invoke(
                getAccessTokenUseCase.invoke().getOrNull().toString(),
                stage,
                number,
                answer
            )
                .onSuccess {
                    _checkAnswerState.value = UiState.Success(it)
                    LoggerUtils.d("답 확인 성공: $it")
                }
                .onFailure {
                    _checkAnswerState.value = UiState.Failure(message = "답 확인 실패")
                    LoggerUtils.e("답 확인 실패")
                }
        }
    }

}
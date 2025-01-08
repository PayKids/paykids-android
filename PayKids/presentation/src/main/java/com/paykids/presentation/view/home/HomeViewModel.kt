package com.paykids.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.usecase.quiz.GetStageNameUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStageNameUseCase: GetStageNameUseCase,
) : ViewModel() {

    private val _stageNameState = MutableLiveData<UiState<String>>(UiState.Loading)
    val stageNameState: LiveData<UiState<String>> get() = _stageNameState

    fun getStageName(stage: Int) {
        _stageNameState.value = UiState.Loading

        viewModelScope.launch {
            getStageNameUseCase.invoke(stage)
                .onSuccess {
                    _stageNameState.value =
                        UiState.Success(it)
                    LoggerUtils.d("스테이지 이름 조회 성공")
                }
                .onFailure {
                    _stageNameState.value = UiState.Failure(message = "스테이지 이름 조회 실패")
                    LoggerUtils.e("스테이지 이름 조회 실패")
                }
        }
    }


}
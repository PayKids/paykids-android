package com.paykids.presentation.view.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.ChatItem
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class StudyViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<ChatItem>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<ChatItem>>> = _uiState

    private val _resState = MutableLiveData<UiState<String>>(UiState.Loading)
    val resState: LiveData<UiState<String>> = _resState

    fun sendQuestion(question: String) {
//        _resState.value = UiState.Loading
//
//        viewModelScope.launch {
//            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()
//
//            sendChatUseCase.invoke(accessToken, question)
//                .onSuccess {
//                    _resState.value = UiState.Success(it)
//                }
//                .onFailure {
//                    _resState.value = UiState.Failure(message = it.message.toString())
//                }
//        }
    }
}
package com.paykids.presentation.view.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.usecase.chat.SendChatUseCase
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.user.GetUserInfoUseCase
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val sendChatUseCase: SendChatUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _userNickname = MutableLiveData<String>()
    val userNickname: LiveData<String> get() = _userNickname

    fun setUserNickname(nickname: String) {
        _userNickname.value = nickname
    }

    private val _userInfoState = MutableLiveData<UiState<UserInfo>>(UiState.Loading)
    val userInfoState: LiveData<UiState<UserInfo>> get() = _userInfoState

    fun getUserInfo() {
        _userInfoState.value = UiState.Loading

        viewModelScope.launch {
            getUserInfoUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _userInfoState.value =
                        UiState.Success(UserInfo(it.nickname, it.email, it.profileImageURL))
                }
                .onFailure {
                    _userInfoState.value = UiState.Failure(message = "유저 정보 불러오기 실패")
                }
        }
    }

    private val _resState = MutableLiveData<UiState<String>>(UiState.Loading)
    val resState: LiveData<UiState<String>> = _resState

    fun sendQuestion(question: String) {
        _resState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().toString()

            sendChatUseCase.invoke(accessToken, question)
                .onSuccess {
                    _resState.value = UiState.Success(it)
                }
                .onFailure {
                    _resState.value = UiState.Failure(message = it.message.toString())
                }
        }
    }
}
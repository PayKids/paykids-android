package com.paykids.presentation.view.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.usecase.auth.SignOutUseCase
import com.paykids.domain.usecase.auth.WithdrawalUseCase
import com.paykids.domain.usecase.datastore.ClearUserDataUseCase
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.user.ChangeNicknameUseCase
import com.paykids.domain.usecase.user.GetUserInfoUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val changeNicknameUseCase: ChangeNicknameUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

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

    private val _nickChangeState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val nickChangeState: LiveData<UiState<Unit>> get() = _nickChangeState

    fun changeNickname(nickname: String) {
        _nickChangeState.value = UiState.Loading

        viewModelScope.launch {
            changeNicknameUseCase(
                getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                nickname
            ).onSuccess {
                _nickChangeState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _nickChangeState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private var _signOutState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val signOutState: LiveData<UiState<Unit>> get() = _signOutState

    private val _withdrawState = MutableLiveData<UiState<String>>(UiState.Loading)
    val withdrawState: LiveData<UiState<String>> get() = _withdrawState

    private val _clearState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val clearState: LiveData<UiState<Boolean>> get() = _clearState

    fun signOut() {
        _signOutState.value = UiState.Loading

        viewModelScope.launch {
            signOutUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _signOutState.value = UiState.Success(Unit)
                    LoggerUtils.d("로그아웃 성공")
                }
                .onFailure {
                    _signOutState.value = UiState.Failure(message = "로그아웃 실패")
                    LoggerUtils.e("로그아웃 실패")
                }
        }
    }

    fun withdraw() {
        _withdrawState.value = UiState.Loading

        viewModelScope.launch {
            withdrawalUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _signOutState.value = UiState.Success(Unit)
                    LoggerUtils.d("회원 탈퇴 성공")
                }
                .onFailure {
                    _signOutState.value = UiState.Failure(message = "회원 탈퇴 실패")
                    LoggerUtils.e("회원 탈퇴 실패")
                }
        }
    }

    fun clearData() {
        _clearState.value = UiState.Loading

        viewModelScope.launch {
            try {
                clearUserDataUseCase()
                    .onSuccess {
                        _clearState.value = UiState.Success(it)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Clear User Data failed: ${e.message}")
                        _clearState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Clear User Data: ${e.message}")
                _clearState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}
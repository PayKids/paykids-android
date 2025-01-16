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
import com.paykids.domain.usecase.user.DeleteUserUseCase
import com.paykids.domain.usecase.user.GetUserInfoUseCase
import com.paykids.domain.usecase.user.UpdateProfileImageUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val changeNicknameUseCase: ChangeNicknameUseCase,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
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

    private val _uploadImageState = MutableLiveData<UiState<String>>(UiState.Loading)
    val uploadImageState: LiveData<UiState<String>> get() = _uploadImageState

    fun uploadProfileImage(file: File, mimeType: String) {
        _uploadImageState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().toString()

            updateProfileImageUseCase(accessToken, file, mimeType)
                .onSuccess {
                    _uploadImageState.value = UiState.Success(it)
                    LoggerUtils.d("Profile image uploaded successfully")
                }
                .onFailure { e ->
                    _uploadImageState.value = UiState.Failure(message = e.message.toString())
                    LoggerUtils.e("Profile image upload failed: ${e.message}")
                }
        }
    }

    private var _signOutState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val signOutState: LiveData<UiState<Unit>> get() = _signOutState

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

    private val _socialWithdrawState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val socialWithdrawState: LiveData<UiState<Unit>> get() = _socialWithdrawState

    fun socialWithdraw() {
        _socialWithdrawState.value = UiState.Loading

        viewModelScope.launch {
            withdrawalUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _socialWithdrawState.value = UiState.Success(Unit)
                    LoggerUtils.d("소셜 회원 탈퇴 성공")
                }
                .onFailure {
                    _socialWithdrawState.value = UiState.Failure(message = "소셜 회원 탈퇴 실패")
                    LoggerUtils.e("소셜 회원 탈퇴 실패")
                }
        }
    }

    private val _serverWithdrawState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val serverWithdrawState: LiveData<UiState<Unit>> get() = _serverWithdrawState

    fun serverWithdraw() {
        _serverWithdrawState.value = UiState.Loading

        viewModelScope.launch {
            deleteUserUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _serverWithdrawState.value = UiState.Success(Unit)
                    LoggerUtils.d("서버 회원 탈퇴 성공")
                }
                .onFailure {
                    _serverWithdrawState.value = UiState.Failure(message = "서버 회원 탈퇴 실패")
                    LoggerUtils.e("서버 회원 탈퇴 실패")
                }
        }
    }

    private val _clearState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val clearState: LiveData<UiState<Unit>> get() = _clearState

    fun clearData() {
        _clearState.value = UiState.Loading

        viewModelScope.launch {
            try {
                clearUserDataUseCase()
                    .onSuccess {
                        _clearState.value = UiState.Success(Unit)
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
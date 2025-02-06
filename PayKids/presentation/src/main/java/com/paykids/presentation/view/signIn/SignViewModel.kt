package com.paykids.presentation.view.signIn

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.auth.SignInInfo
import com.paykids.domain.model.auth.UserSignInInfo
import com.paykids.domain.usecase.auth.KakaoAuthUseCase
import com.paykids.domain.usecase.auth.SaveSignInInfoUseCase
import com.paykids.domain.usecase.auth.SignInUseCase
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.user.SaveNicknameUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val kakaoAuthUseCase: KakaoAuthUseCase,
    private val signInUseCase: SignInUseCase,
    private val saveSignInInfoUseCase: SaveSignInInfoUseCase,
    private val saveNicknameUseCase: SaveNicknameUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _checkTokenState = MutableLiveData<UiState<String>>()
    val checkTokenState: LiveData<UiState<String>> get() = _checkTokenState

    fun checkToken() {
        _checkTokenState.value = UiState.Loading
        viewModelScope.launch {
            try {
                getAccessTokenUseCase.invoke()
                    .onSuccess {
                        _checkTokenState.value = UiState.Success(it)
                    }.onFailure { e ->
                        _checkTokenState.value =
                            UiState.Failure(message = e.message ?: "저장된 토큰 확인 실패")
                    }
            } catch (e: Exception) {
                _checkTokenState.value = UiState.Failure(message = e.message ?: "저장된 토큰 확인 중 예외 발생")
            }
        }
    }

    private val _kakaoLoginState = MutableLiveData<UiState<SignInInfo>>()
    val kakaoLoginState: LiveData<UiState<SignInInfo>> get() = _kakaoLoginState

    fun signInWithKakao(context: Context) {
        _kakaoLoginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                kakaoAuthUseCase.invoke(context)
                    .onSuccess { signInInfo ->
                        _kakaoLoginState.value = UiState.Success(signInInfo)
                        signIn(signInInfo.idToken)
                    }.onFailure { e ->
                        _kakaoLoginState.value =
                            UiState.Failure(message = e.message ?: "카카오 로그인 실패")
                    }
            } catch (e: Exception) {
                _kakaoLoginState.value = UiState.Failure(message = e.message ?: "카카오 로그인 중 예외 발생")
            }
        }
    }

    private val _loginState = MutableLiveData<UiState<UserSignInInfo>>()
    val loginState: LiveData<UiState<UserSignInInfo>> get() = _loginState

    fun signIn(idToken: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                signInUseCase.invoke(idToken)
                    .onSuccess { userSignInInfo ->
                        _loginState.value = UiState.Success(userSignInInfo)
                    }.onFailure { e ->
                        _loginState.value = UiState.Failure(message = e.message ?: "페이키즈 로그인 실패")
                    }
            } catch (e: Exception) {
                _loginState.value = UiState.Failure(message = e.message ?: "페이키즈 로그인 중 예외 발생")
            }
        }
    }

    private val _saveState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val saveState: LiveData<UiState<Boolean>> get() = _saveState

    fun saveSignInInfo(info: UserSignInInfo) {
        _saveState.value = UiState.Loading

        viewModelScope.launch {
            try {
                saveSignInInfoUseCase(info.accessToken, info.refreshToken)
                    .onSuccess {
                        _saveState.value = UiState.Success(info.isRegistered)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("save Sign-in Info failed: ${e.message}")
                        _saveState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("save Sign-in Info exception: ${e.message}")
                _saveState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _nickState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val nickState: LiveData<UiState<Unit>> get() = _nickState

    fun saveNickname(nickname: String) {
        _nickState.value = UiState.Loading

        viewModelScope.launch {
            saveNicknameUseCase(
                getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                nickname
            ).onSuccess {
                _nickState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _nickState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}
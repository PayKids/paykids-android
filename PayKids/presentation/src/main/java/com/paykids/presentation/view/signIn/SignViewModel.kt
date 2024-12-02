package com.paykids.presentation.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.usecase.auth.SignInUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    var isRegister = false

    private val _loginState = MutableLiveData<UiState<SignInInfo>>()
    val loginState: LiveData<UiState<SignInInfo>> get() = _loginState

    fun signInWithKakao() {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                signInUseCase.invoke("", AuthProvider.KAKAO)
                    .onSuccess { signInInfo ->
                        LoggerUtils.d("로그인 성공: $signInInfo")
                        _loginState.value = UiState.Success(signInInfo)
                    }.onFailure { e ->
                        _loginState.value = UiState.Failure(message = e.message ?: "카카오 로그인 실패")
                    }
            } catch (e: Exception) {
                _loginState.value = UiState.Failure(message = e.message ?: "카카오 로그인 중 예외 발생")
            }
        }
    }
}
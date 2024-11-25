package com.paykids.presentation.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.usecase.SignInUseCase
import com.paykids.presentation.utils.LoggerUtils
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val kakaoAuthRepository: KakaoAuthRepository,
    private val signInUseCase: SignInUseCase,
) : ViewModel() {

    private val _loginState =
        MutableLiveData<UiState<SignInInfo>>(UiState.Loading)
    val loginState: LiveData<UiState<SignInInfo>> get() = _loginState

    fun signIn(idToken: String, provider: AuthProvider) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val result = signInUseCase(idToken, provider)
                if (result.isSuccess) {
                    _loginState.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _loginState.value = UiState.Failure(message = "로그인 실패")
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Failure(message = e.message ?: "로그인 실패")
            }
        }
    }

    fun signInWithKakao() {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val signInInfo = kakaoAuthRepository.signInWithKakao()
                _loginState.value = UiState.Success(signInInfo)
            } catch (e: Exception) {
                _loginState.value = UiState.Failure(message = e.message ?: "카카오 로그인 실패")
            }
        }
    }
}
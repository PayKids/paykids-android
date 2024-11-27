package com.paykids.presentation.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.usecase.auth.KakaoAuthUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val kakaoAuthUseCase: KakaoAuthUseCase,
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<SignInInfo>>(UiState.Loading)
    val loginState: LiveData<UiState<SignInInfo>> get() = _loginState

    fun signInWithKakao() {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val signInInfo = kakaoAuthUseCase()
                _loginState.value = UiState.Success(signInInfo)
                LoggerUtils.d("로그인 성공!: $signInInfo")
            } catch (e: Exception) {
                _loginState.value = UiState.Failure(message = e.message ?: "카카오 로그인 실패")
            }
        }
    }
}
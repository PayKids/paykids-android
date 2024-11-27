package com.paykids.presentation.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.usecase.auth.SignOutUseCase
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private var _signOutState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val signOutState get() = _signOutState

    fun signOut() {
        _signOutState.value = UiState.Loading

        viewModelScope.launch {
            signOutUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _signOutState.value = UiState.Success(Unit)
                    LoggerUtils.d("Sign Out 성공")
                }
                .onFailure {
                    _signOutState.value = UiState.Failure(message = "SignOut 실패")
                    LoggerUtils.e("Sign Out 실패")
                }
        }
    }
}
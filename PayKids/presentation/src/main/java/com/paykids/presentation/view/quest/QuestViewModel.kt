package com.paykids.presentation.view.quest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.usecase.acievement.GetAchievementUseCase
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
class QuestViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getAchievementUseCase: GetAchievementUseCase
) : ViewModel() {

    private val _questState = MutableLiveData<UiState<UserInfo>>(UiState.Loading)
    val questState: LiveData<UiState<UserInfo>> get() = _questState

    fun getQuests() {
        _questState.value = UiState.Loading

        viewModelScope.launch {
            getAchievementUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
//                    _questState.value = UiState.Success(it)
                }
                .onFailure {
                    _questState.value = UiState.Failure(message = "업적 정보 불러오기 실패")
                }
        }
    }

    private val _achievementState = MutableLiveData<UiState<List<AchievementInfo>>>(UiState.Loading)
    val achievementState: LiveData<UiState<List<AchievementInfo>>> get() = _achievementState

    fun getAchievements() {
        _achievementState.value = UiState.Loading

        viewModelScope.launch {
            getAchievementUseCase.invoke(getAccessTokenUseCase.invoke().getOrNull().toString())
                .onSuccess {
                    _achievementState.value = UiState.Success(it)
                }
                .onFailure {
                    _achievementState.value = UiState.Failure(message = "업적 정보 불러오기 실패")
                }
        }
    }

}
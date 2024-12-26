package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paykids.domain.model.DetailConsume
import com.paykids.domain.model.DiaryInfo
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _diaryState = MutableLiveData<UiState<List<DiaryInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<DiaryInfo>>> get() = _diaryState

    private val _selectedDateDetails = MutableLiveData<List<DetailConsume>>()
    val selectedDateDetails: LiveData<List<DetailConsume>> get() = _selectedDateDetails

    fun fetchDetailsForDate(date: String) {
        val dummyData = when (date) {
            "2024-12-25" -> listOf(
                DetailConsume("편의점", -5000, "크리스마스 기념 구매"),
                DetailConsume("카페", -4500, "크리스마스 커피")
            )

            else -> listOf(
                DetailConsume("슈퍼마켓", -1500, "일반 구매"),
                DetailConsume("서점", -2500, "책 구매")
            )
        }
        _selectedDateDetails.value = dummyData
    }

//    fun fetchData(year: Int, month: Int) {
//        _diaryState.value = UiState.Loading
//
//        viewModelScope.launch {
//            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()
//
//            getMonthlyDiaryUseCase(accessToken, year, month)
//                .onSuccess {
//                    _diaryState.value = UiState.Success(it)
//                }.onFailure { e ->
//                    _diaryState.value = UiState.Failure(message = e.message.toString())
//                }
//        }
//    }
}
package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paykids.domain.model.DayInfo
import com.paykids.domain.model.DetailConsume
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.presentation.utils.Constants
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _diaryState = MutableLiveData<UiState<List<DayInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<DayInfo>>> get() = _diaryState

    private val _selectedDateDetails = MutableLiveData<List<DetailConsume>>()
    val selectedDateDetails: LiveData<List<DetailConsume>> get() = _selectedDateDetails

    fun fetchDetailsForDate(date: String) {
        val dummyData = when (date) {
            "2024-12-25" -> listOf(
                DetailConsume("2024-12-25", "편의점", 5000, "크리스마스 기념 구매"),
                DetailConsume("2024-12-25", "카페", 4500, "크리스마스 커피")
            )

            "2024-12-28" -> listOf(
                DetailConsume("2024-12-28", "방탈출", 28000, "필름바이스티브"),
                DetailConsume("2024-12-28", "보드게임", 8000, "버건디의 성")
            )

            "2024-12-20" -> listOf(
                DetailConsume("2024-12-20", "서점", 22200, "일반 구매"),
                DetailConsume("2024-12-20", "서점", 25000, "책 구매")
            )

            else -> emptyList()
        }

        _selectedDateDetails.value = dummyData
    }

    private val _dayInfoList = MutableLiveData<List<DayInfo>>()
    val dayInfoList: LiveData<List<DayInfo>> get() = _dayInfoList

    fun fetchDayInfo() {
        _dayInfoList.value = listOf(
            DayInfo("2024-12-18", 200000, 4000),
            DayInfo("2024-12-25", 0, 9500),
            DayInfo("2024-12-28", 0, 36000)
        )
    }

    fun getDayInfoForMonth(yearMonth: String): List<DayInfo> {
        return _dayInfoList.value?.filter { it.date.startsWith(yearMonth) } ?: emptyList()
    }

    fun getMonthConsumption(yearMonth: String): Int {
        return _dayInfoList.value?.filter { it.date.startsWith(yearMonth) }
            ?.sumOf { it.consume } ?: 0
    }

    fun getMostConsumedCategoryForMonth(yearMonth: String): Pair<String, Int>? {
        // 일단 25일 소비로 화면 갱신
        fetchDetailsForDate("2024-12-25")
        val detailsForMonth =
            _selectedDateDetails.value?.filter { it.date.startsWith(yearMonth) } ?: emptyList()

        LoggerUtils.d(detailsForMonth.toString())

        val categoryTotalMap = detailsForMonth.groupingBy { it.place }
            .fold(0) { total, detail -> total + detail.amount }

        return categoryTotalMap.maxByOrNull { it.value }?.toPair()
    }

}
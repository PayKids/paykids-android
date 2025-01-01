package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paykids.domain.model.CategoryInfo
import com.paykids.domain.model.DayInfo
import com.paykids.domain.model.DetailConsume
import com.paykids.domain.model.DetailIncome
import com.paykids.domain.model.DetailTransaction
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _monthlyAllInfo = MutableLiveData<List<DetailTransaction>>(emptyList())
    val monthlyAllInfo: LiveData<List<DetailTransaction>> get() = _monthlyAllInfo

    fun fetchMonthlyData() {
        _monthlyAllInfo.value = listOf(
            DetailConsume("2024-12-25", "편의점", 5000, "크리스마스 기념 구매"),
            DetailConsume("2024-12-25", "카페", 4500, "크리스마스 커피"),
            DetailConsume("2024-12-28", "방탈출", 28000, "필름바이스티브"),
            DetailConsume("2024-12-28", "보드게임", 8000, "버건디의 성"),
            DetailConsume("2024-12-20", "서점", 22200, "일반 구매"),
            DetailConsume("2024-12-20", "서점", 25000, "책 구매"),
            DetailConsume("2025-01-01", "방탈출", 18000, "어린왕자"),
            DetailConsume("2025-01-01", "보드게임", 10000, "커피러쉬"),
            DetailConsume("2025-01-02", "서점", 12500, "책 구매"),
            DetailIncome("2024-12-10", "용돈", 100000, "12월 용돈"),
            DetailIncome("2025-01-01", "용돈", 12500, "1월 용돈"),
            DetailIncome("2025-01-02", "기타", 99500, "길을 가다가 주웠다!")
        )
    }

    private val _selectedDateDetails = MutableLiveData<List<DetailConsume>>(emptyList())
    val selectedDateDetails: LiveData<List<DetailConsume>> get() = _selectedDateDetails

    fun fetchDetailsForDate(date: String) {
        val detailsForDate = _monthlyAllInfo.value?.filterIsInstance<DetailConsume>()?.filter { it.date == date } ?: emptyList()
        _selectedDateDetails.value = detailsForDate
    }

    private val _dayInfoList = MutableLiveData<List<DayInfo>>()
    val dayInfoList: LiveData<List<DayInfo>> get() = _dayInfoList

    fun fetchDayInfo() {
        val dayInfos = _monthlyAllInfo.value?.let { monthlyAllInfo ->
            // _monthlyAllInfo.value가 null이 아니면 그 값을 사용하여 처리
            monthlyAllInfo.map { detail ->
                val date = detail.date

                // 해당 날짜의 DetailIncome 합산
                val totalIncomeAmount = monthlyAllInfo.filterIsInstance<DetailIncome>()
                    .filter { it.date == date }
                    .sumOf { it.amount }

                // 해당 날짜의 DetailConsume 합산
                val totalConsumeAmount = monthlyAllInfo.filterIsInstance<DetailConsume>()
                    .filter { it.date == date }
                    .sumOf { it.amount }

                DayInfo(date, totalIncomeAmount, totalConsumeAmount)
            }.distinctBy { it.date } // 중복 날짜 제거
        } ?: emptyList()

        _dayInfoList.value = dayInfos
    }

    fun getDayInfoForMonth(yearMonth: String): List<DayInfo> {
        return _dayInfoList.value?.filter { it.date.startsWith(yearMonth) } ?: emptyList()
    }

    fun getMonthConsumption(yearMonth: String): Int {
        return _monthlyAllInfo.value?.filter { it.date.startsWith(yearMonth) }
            ?.sumOf { it.amount } ?: 0
    }

    fun getMonthlyCostCategory(): List<CategoryInfo> {
        val monthlyData = _monthlyAllInfo.value ?: emptyList()

        // 총 지출 금액 계산
        val totalAmount = monthlyData.sumOf { it.amount }

        // 카테고리별 데이터 그룹화 및 합계 계산
        return monthlyData
            .groupBy { it.category }
            .map { (category, details) ->
                val categorySum = details.sumOf { it.amount }
                val percentage = if (totalAmount > 0) (categorySum * 100) / totalAmount else 0
                CategoryInfo(category, categorySum, percentage)
            }
    }

    fun getMostConsumedCategoryForMonth(yearMonth: String): Pair<String, Int>? {
        fetchMonthlyData()

        val detailsForMonth =
            _monthlyAllInfo.value?.filter { it.date.startsWith(yearMonth) } ?: emptyList()

        val categoryTotalMap = detailsForMonth.groupingBy { it.category }
            .fold(0) { total, detail -> total + detail.amount }

        return categoryTotalMap.maxByOrNull { it.value }?.toPair()
    }

    fun getConsumptionByCategory(category: String): List<Triple<String, Int, String>> {
        return _monthlyAllInfo.value
            ?.filter { it.category == category }
            ?.map { Triple(it.date, it.amount, it.memo) }
            ?: emptyList()
    }
}
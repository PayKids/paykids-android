package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.CategoryInfo
import com.paykids.domain.model.DayInfo
import com.paykids.domain.model.DetailConsume
import com.paykids.domain.model.DetailIncome
import com.paykids.domain.model.DetailTransaction
import com.paykids.domain.model.expense.DailyExpenseInfo
import com.paykids.domain.model.expense.MonthMostCategory
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.expense.GetMonthDailyExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthMostCategoryUseCase
import com.paykids.domain.usecase.expense.GetMonthTotalExpenseUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getMonthTotalExpenseUseCase: GetMonthTotalExpenseUseCase,
    private val getMonthDailyExpenseUseCase: GetMonthDailyExpenseUseCase,
    private val getMonthMostCategoryUseCase: GetMonthMostCategoryUseCase
) : ViewModel() {

    private val _monthTotalExpenseState = MutableLiveData<UiState<Int>>()
    val monthTotalExpenseState: LiveData<UiState<Int>> get() = _monthTotalExpenseState

    fun getMonthTotalExpense(year: Int, month: Int) {
        _monthTotalExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthTotalExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _monthTotalExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e("get Month Total Expense failed: ${e.message}")
                    _monthTotalExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Total Expense exception: ${e.message}")
                _monthTotalExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _monthMostCategoryState = MutableLiveData<UiState<MonthMostCategory>>()
    val monthMostCategoryState: LiveData<UiState<MonthMostCategory>> get() = _monthMostCategoryState

    fun getMonthMostCategory(year: Int, month: Int) {
        _monthMostCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthMostCategoryUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _monthMostCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e("get Month Most Category failed: ${e.message}")
                    _monthMostCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Most Category exception: ${e.message}")
                _monthMostCategoryState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _monthDailyExpenseState = MutableLiveData<UiState<List<DailyExpenseInfo>>>()
    val monthDailyExpenseState: LiveData<UiState<List<DailyExpenseInfo>>> get() = _monthDailyExpenseState

    fun getMonthDailyExpense(year: Int, month: Int) {
        _monthDailyExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthDailyExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _monthDailyExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e("get Month Daily Expense failed: ${e.message}")
                    _monthDailyExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Daily Expense exception: ${e.message}")
                _monthDailyExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

//    fun getDayInfoForMonth(yearMonth: String): List<DayInfo> {
//        return dayInfoList.value?.filter { it.date.startsWith(yearMonth) } ?: emptyList()
//    }
//
//    fun getMonthConsumption(yearMonth: String): Int {
//        return monthlyAllInfo.value?.filterIsInstance<DetailConsume>()
//            ?.filter { it.date.startsWith(yearMonth) } // 해당 년월에 해당하는 항목 필터링
//            ?.sumOf { it.amount }
//            ?: 0 // null일 경우 0 반환
//    }
//
//    fun getMonthlyCostCategory(yearMonth: String): List<CategoryInfo> {
//        val monthlyData = monthlyAllInfo.value?.filterIsInstance<DetailConsume>()
//            ?.filter { it.date.startsWith(yearMonth) } ?: emptyList()
//
//        // 총 지출 금액 계산
//        val totalAmount = monthlyData.sumOf { it.amount }
//
//        // 카테고리별 데이터 그룹화 및 합계 계산
//        return monthlyData
//            .groupBy { it.category }
//            .map { (category, details) ->
//                val categorySum = details.sumOf { it.amount }
//                val percentage = if (totalAmount > 0) (categorySum * 100) / totalAmount else 0
//                CategoryInfo(category, categorySum, percentage)
//            }
//    }
//
//    fun getMostConsumedCategoryForMonth(yearMonth: String): Pair<String, Int>? {
//        fetchMonthlyData()
//
//        val detailsForMonth =
//            monthlyAllInfo.value?.filterIsInstance<DetailConsume>()
//                ?.filter { it.date.startsWith(yearMonth) } ?: emptyList()
//
//        val categoryTotalMap = detailsForMonth.groupingBy { it.category }
//            .fold(0) { total, detail -> total + detail.amount }
//
//        return categoryTotalMap.maxByOrNull { it.value }?.toPair()
//    }
//
//    fun getConsumptionByCategory(category: String): List<Triple<String, Int, String>> {
//        return monthlyAllInfo.value
//            ?.filterIsInstance<DetailConsume>()
//            ?.filter { it.category == category } // 카테고리별 필터링
//            ?.map { Triple(it.date, it.amount, it.memo) }
//            ?: emptyList()
//    }
//
//    fun addTransaction(transaction: DetailTransaction) {
//        val updatedList = _monthlyAllInfo.value.orEmpty() + transaction
//        _monthlyAllInfo.value = updatedList
//
//        fetchDetailsForDate(transaction.date)
//        fetchDayInfo()
//    }

}
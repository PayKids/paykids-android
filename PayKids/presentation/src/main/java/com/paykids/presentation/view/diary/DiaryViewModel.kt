package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.model.allowance.MonthMostCategory
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.expense.AddExpenseUseCase
import com.paykids.domain.usecase.expense.GetDayExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthAllCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthDailyExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthMostCategoryUseCase
import com.paykids.domain.usecase.expense.GetMonthTotalExpenseUseCase
import com.paykids.domain.usecase.expense.UpdateExpenseUseCase
import com.paykids.domain.usecase.income.GetMonthAllCategoryIncomeUseCase
import com.paykids.domain.usecase.income.GetMonthDailyIncomeUseCase
import com.paykids.domain.usecase.income.GetMonthTotalIncomeUseCase
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
    private val getMonthMostCategoryUseCase: GetMonthMostCategoryUseCase,
    private val getMonthCategoryExpenseUseCase: GetMonthCategoryExpenseUseCase,
    private val getMonthAllCategoryExpenseUseCase: GetMonthAllCategoryExpenseUseCase,
    private val getDayExpenseUseCase: GetDayExpenseUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val getMonthTotalIncomeUseCase: GetMonthTotalIncomeUseCase,
    private val getMonthDailyIncomeUseCase: GetMonthDailyIncomeUseCase,
    private val getMonthAllCategoryIncomeUseCase: GetMonthAllCategoryIncomeUseCase
) : ViewModel() {

    private val _currentMonthData = MutableLiveData<Pair<Int, Int>>()
    val currentMonthData: LiveData<Pair<Int, Int>> get() = _currentMonthData

    fun updateMonth(year: Int, month: Int) {
        _currentMonthData.value = Pair(year, month)
    }

    private val _monthTotalExpenseState = MutableLiveData<UiState<Int>>()
    val monthTotalExpenseState: LiveData<UiState<Int>> get() = _monthTotalExpenseState

    fun getMonthTotalExpense(year: Int, month: Int) {
        _monthTotalExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthTotalIncomeUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _monthTotalExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _monthTotalExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Total Expense exception: ${e.message}")
                _monthTotalExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _monthTotalIncomeState = MutableLiveData<UiState<Int>>()
    val monthTotalIncomeState: LiveData<UiState<Int>> get() = _monthTotalIncomeState

    fun getMonthTotalIncome(year: Int, month: Int) {
        _monthTotalIncomeState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthTotalIncomeUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _monthTotalIncomeState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _monthTotalIncomeState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Total Income exception: ${e.message}")
                _monthTotalIncomeState.value = UiState.Failure(message = e.message.toString())
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
                    LoggerUtils.e(e.message.toString())
                    _monthMostCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Most Category exception: ${e.message}")
                _monthMostCategoryState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _monthDailyExpenseState = MutableLiveData<UiState<List<MonthDailyInfo>>>()
    val monthDailyExpenseState: LiveData<UiState<List<MonthDailyInfo>>> get() = _monthDailyExpenseState

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
                    LoggerUtils.e(e.message.toString())
                    _monthDailyExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Daily Expense exception: ${e.message}")
                _monthDailyExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _dayExpenseState = MutableLiveData<UiState<List<DayInfo>>>()
    val dayExpenseState: LiveData<UiState<List<DayInfo>>> get() = _dayExpenseState

    fun getDayExpense(date: String) {
        _dayExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getDayExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    date
                ).onSuccess {
                    _dayExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _dayExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Day Expense exception: ${e.message}")
                _dayExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _allExpenseCategoryState = MutableLiveData<UiState<List<MonthAllCategoryInfo>>>()
    val allExpenseCategoryState: LiveData<UiState<List<MonthAllCategoryInfo>>> get() = _allExpenseCategoryState

    fun getMonthAllExpenseCategory(year: Int, month: Int) {
        _allExpenseCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthAllCategoryExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _allExpenseCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _allExpenseCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Expense All Category exception: ${e.message}")
                _allExpenseCategoryState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _allIncomeCategoryState = MutableLiveData<UiState<List<MonthAllCategoryInfo>>>()
    val allIncomeCategoryState: LiveData<UiState<List<MonthAllCategoryInfo>>> get() = _allIncomeCategoryState

    fun getMonthAllIncomeCategory(year: Int, month: Int) {
        _allIncomeCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthAllCategoryIncomeUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _allIncomeCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _allIncomeCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get All Income Category exception: ${e.message}")
                _allIncomeCategoryState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _categoryExpenseState = MutableLiveData<UiState<List<MonthCategoryInfo>>>()
    val categoryExpenseState: LiveData<UiState<List<MonthCategoryInfo>>> get() = _categoryExpenseState

    fun getMonthCategoryExpense(year: Int, month: Int, category: String) {
        _categoryExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthCategoryExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month, category
                ).onSuccess {
                    _categoryExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _categoryExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Category Expense exception: ${e.message}")
                _categoryExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _saveExpenseState = MutableLiveData<UiState<Boolean>>()
    val saveExpenseState: LiveData<UiState<Boolean>> get() = _saveExpenseState

    fun saveExpense(
        date: String,
        allowanceType: String,
        amount: Int,
        memo: String,
        category: String
    ) {
        _saveExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                addExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    date, allowanceType, category, amount, memo
                ).onSuccess {
                    _saveExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _saveExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("save Expense exception: ${e.message}")
                _saveExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _updateExpenseState = MutableLiveData<UiState<Boolean>>()
    val updateExpenseState: LiveData<UiState<Boolean>> get() = _updateExpenseState

    fun updateExpense(
        id: Int,
        date: String,
        allowanceType: String,
        amount: Int,
        memo: String,
        category: String
    ) {
        _updateExpenseState.value = UiState.Loading

        viewModelScope.launch {
            try {
                updateExpenseUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    id, date, allowanceType, category, amount, memo
                ).onSuccess {
                    _updateExpenseState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _updateExpenseState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("update Expense exception: ${e.message}")
                _updateExpenseState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _monthDailyIncomeState = MutableLiveData<UiState<List<MonthDailyInfo>>>()
    val monthDailyIncomeState: LiveData<UiState<List<MonthDailyInfo>>> get() = _monthDailyIncomeState

    fun getMonthDailyIncome(year: Int, month: Int) {
        _monthDailyIncomeState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getMonthDailyIncomeUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    year, month
                ).onSuccess {
                    _monthDailyIncomeState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _monthDailyIncomeState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("get Month Daily Income exception: ${e.message}")
                _monthDailyIncomeState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

}
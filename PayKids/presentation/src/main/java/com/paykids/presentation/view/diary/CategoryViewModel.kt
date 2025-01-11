package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.expenseCategory.AddExpenseCategoryUseCase
import com.paykids.domain.usecase.expenseCategory.DeleteExpenseCategoryUseCase
import com.paykids.domain.usecase.incomeCategory.AddIncomeCategoryUseCase
import com.paykids.domain.usecase.incomeCategory.DeleteIncomeCategoryUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val addExpenseCategoryUseCase: AddExpenseCategoryUseCase,
    private val addIncomeCategoryUseCase: AddIncomeCategoryUseCase,
    private val deleteExpenseCategoryUseCase: DeleteExpenseCategoryUseCase,
    private val deleteIncomeCategoryUseCase: DeleteIncomeCategoryUseCase
) : ViewModel() {

    private val _addExpenseCategoryState = MutableLiveData<UiState<Boolean>>()
    val addExpenseCategoryState: LiveData<UiState<Boolean>> get() = _addExpenseCategoryState

    fun addExpenseCategory(name: String) {
        _addExpenseCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                addExpenseCategoryUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    name
                ).onSuccess {
                    _addExpenseCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _addExpenseCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("add Expense Category exception: ${e.message}")
                _addExpenseCategoryState.value =
                    UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _addIncomeCategoryState = MutableLiveData<UiState<Boolean>>()
    val addIncomeCategoryState: LiveData<UiState<Boolean>> get() = _addIncomeCategoryState

    fun addIncomeCategory(name: String) {
        _addIncomeCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                addIncomeCategoryUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    name
                ).onSuccess {
                    _addIncomeCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _addIncomeCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("add Income Category exception: ${e.message}")
                _addIncomeCategoryState.value =
                    UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _deleteExpenseCategoryState = MutableLiveData<UiState<Boolean>>()
    val deleteExpenseCategoryState: LiveData<UiState<Boolean>> get() = _deleteExpenseCategoryState

    fun deleteExpenseCategory(name: String) {
        _deleteExpenseCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                deleteExpenseCategoryUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    name
                ).onSuccess {
                    _deleteExpenseCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _deleteExpenseCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("delete Expense Category exception: ${e.message}")
                _deleteExpenseCategoryState.value =
                    UiState.Failure(message = e.message.toString())
            }
        }
    }

    private val _deleteIncomeCategoryState = MutableLiveData<UiState<Boolean>>()
    val deleteIncomeCategoryState: LiveData<UiState<Boolean>> get() = _deleteIncomeCategoryState

    fun deleteIncomeCategory(name: String) {
        _deleteIncomeCategoryState.value = UiState.Loading

        viewModelScope.launch {
            try {
                deleteIncomeCategoryUseCase(
                    getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                    name
                ).onSuccess {
                    _deleteIncomeCategoryState.value = UiState.Success(it)
                }.onFailure { e ->
                    LoggerUtils.e(e.message.toString())
                    _deleteIncomeCategoryState.value =
                        UiState.Failure(message = e.message.toString())
                }
            } catch (e: Exception) {
                LoggerUtils.e("delete Income Category exception: ${e.message}")
                _deleteIncomeCategoryState.value =
                    UiState.Failure(message = e.message.toString())
            }
        }
    }

}
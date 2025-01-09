package com.paykids.presentation.view.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.expense.AddExpenseUseCase
import com.paykids.domain.usecase.expenseCategory.DeleteExpenseCategoryUseCase
import com.paykids.domain.usecase.expenseCategory.SaveExpenseCategoryUseCase
import com.paykids.domain.usecase.incomeCategory.DeleteIncomeCategoryUseCase
import com.paykids.domain.usecase.incomeCategory.SaveIncomeCategoryUseCase
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val saveExpenseCategoryUseCase: SaveExpenseCategoryUseCase,
    private val saveIncomeCategoryUseCase: SaveIncomeCategoryUseCase,
    private val deleteExpenseCategoryUseCase: DeleteExpenseCategoryUseCase,
    private val deleteIncomeCategoryUseCase: DeleteIncomeCategoryUseCase
) : ViewModel() {

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
package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expenseIncome.MonthCategoryInfo
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthCategoryExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int, category: String
    ): Result<List<MonthCategoryInfo>> {
        return repository.getMonthCategoryExpense("Bearer $accessToken", year, month, category)
    }
}
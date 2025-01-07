package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expense.MonthMostCategory
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthMostCategoryUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<MonthMostCategory> {
        return repository.getMonthMostExpenseCategory("Bearer $accessToken", year, month)
    }
}
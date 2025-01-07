package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expense.MonthDailyExpenseInfo
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthDailyExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthDailyExpenseInfo>> {
        return repository.getMonthDailyExpense("Bearer $accessToken", year, month)
    }
}
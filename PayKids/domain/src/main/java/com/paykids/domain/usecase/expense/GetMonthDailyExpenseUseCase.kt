package com.paykids.domain.usecase.expense

import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthDailyExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthDailyInfo>> {
        return repository.getMonthDailyExpense("Bearer $accessToken", year, month)
    }
}
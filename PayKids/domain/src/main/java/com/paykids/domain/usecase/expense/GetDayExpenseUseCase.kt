package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expenseIncome.DayInfo
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetDayExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        localDate: String
    ): Result<List<DayInfo>> {
        return repository.getDayExpense("Bearer $accessToken", localDate)
    }
}
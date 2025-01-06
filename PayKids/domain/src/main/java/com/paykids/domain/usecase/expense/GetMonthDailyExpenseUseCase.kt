package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expense.DailyExpenseInfo
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class GetMonthDailyExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<DailyExpenseInfo>> {
        return repository.getMonthDailyExpense("Bearer $accessToken", year, month)
    }
}
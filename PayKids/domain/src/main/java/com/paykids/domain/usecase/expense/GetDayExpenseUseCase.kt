package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expense.DayExpense
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class GetDayExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        localDate: String
    ): Result<List<DayExpense>> {
        return repository.getDayExpense("Bearer $accessToken", localDate)
    }
}
package com.paykids.domain.usecase.expense

import com.paykids.domain.model.expense.MonthCategoryExpense
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class GetMonthCategoryExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int, category: String
    ): Result<List<MonthCategoryExpense>> {
        return repository.getMonthCategoryExpense("Bearer $accessToken", year, month, category)
    }
}
package com.paykids.domain.usecase.expense

import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthAllCategoryExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategoryInfo>> {
        return repository.getMonthAllCategoryExpense("Bearer $accessToken", year, month)
    }
}
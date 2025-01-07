package com.paykids.domain.usecase.expense

import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthAllCategoryUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategoryInfo>> {
        return repository.getMonthAllCategory("Bearer $accessToken", year, month)
    }
}
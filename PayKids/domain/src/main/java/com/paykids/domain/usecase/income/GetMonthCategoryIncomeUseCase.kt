package com.paykids.domain.usecase.income

import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class GetMonthCategoryIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int, category: String
    ): Result<List<MonthCategoryInfo>> {
        return repository.getMonthCategoryIncome("Bearer $accessToken", year, month, category)
    }
}
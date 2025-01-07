package com.paykids.domain.usecase.income

import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class GetMonthAllCategoryIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategoryInfo>> {
        return repository.getMonthAllCategoryIncome("Bearer $accessToken", year, month)
    }
}
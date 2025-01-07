package com.paykids.domain.usecase.income

import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class GetMonthDailyIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthDailyInfo>> {
        return repository.getMonthDailyIncome("Bearer $accessToken", year, month)
    }
}
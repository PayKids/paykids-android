package com.paykids.domain.usecase.income

import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class GetMonthTotalIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int> {
        return repository.getMonthTotalIncome("Bearer $accessToken", year, month)
    }
}
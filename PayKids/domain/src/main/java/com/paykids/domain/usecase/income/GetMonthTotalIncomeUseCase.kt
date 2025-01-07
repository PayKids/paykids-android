package com.paykids.domain.usecase.income

import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class GetMonthTotalIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int> {
        require(year >= 2000) { "유효하지 않은 연도입니다" }
        require(month in 1..12) { "유효하지 않은 월입니다" }

        return repository.getMonthTotalIncome("Bearer $accessToken", year, month)
    }
}
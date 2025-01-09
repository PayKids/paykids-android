package com.paykids.domain.usecase.expense

import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetMonthTotalExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int> {
        require(year >= 2000) { "유효하지 않은 연도입니다" }
        require(month in 1..12) { "유효하지 않은 월입니다" }

        return repository.getMonthTotalExpense("Bearer $accessToken", year, month)
    }
}
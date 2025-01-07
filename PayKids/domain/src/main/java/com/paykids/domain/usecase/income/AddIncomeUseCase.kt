package com.paykids.domain.usecase.income

import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class AddIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        return repository.saveIncome(
            "Bearer $accessToken",
            date,
            allowanceType,
            category,
            amount,
            memo
        )
    }
}
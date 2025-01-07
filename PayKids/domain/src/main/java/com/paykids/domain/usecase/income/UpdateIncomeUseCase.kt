package com.paykids.domain.usecase.income

import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class UpdateIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        id: Int,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        return repository.updateIncome(
            id,
            "Bearer $accessToken",
            date,
            allowanceType,
            category,
            amount,
            memo
        )
    }
}
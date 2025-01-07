package com.paykids.domain.usecase.expense

import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class SaveExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        id: Int,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        return repository.saveExpense(
            "Bearer $accessToken",
            id,
            date,
            allowanceType,
            category,
            amount,
            memo
        )
    }
}
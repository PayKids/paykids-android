package com.paykids.domain.usecase.expense

import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        return repository.saveExpense(
            "Bearer $accessToken",
            date,
            allowanceType,
            category,
            amount,
            memo
        )
    }
}
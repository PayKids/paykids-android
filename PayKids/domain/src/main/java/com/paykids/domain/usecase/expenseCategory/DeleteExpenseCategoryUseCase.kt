package com.paykids.domain.usecase.expenseCategory

import com.paykids.domain.repository.ExpenseCategoryRepository
import javax.inject.Inject

class DeleteExpenseCategoryUseCase @Inject constructor(private val repository: ExpenseCategoryRepository) {
    suspend operator fun invoke(accessToken: String, category: String): Result<Boolean> {
        return repository.deleteExpenseCategory("Bearer $accessToken", category)
    }
}
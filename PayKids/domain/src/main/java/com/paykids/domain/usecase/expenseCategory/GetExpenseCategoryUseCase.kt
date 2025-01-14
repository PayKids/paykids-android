package com.paykids.domain.usecase.expenseCategory

import com.paykids.domain.model.allowanceCategory.CategoryInfo
import com.paykids.domain.repository.ExpenseCategoryRepository
import javax.inject.Inject

class GetExpenseCategoryUseCase @Inject constructor(private val repository: ExpenseCategoryRepository) {
    suspend operator fun invoke(accessToken: String): Result<List<CategoryInfo>> {
        return repository.getExpenseCategoryList("Bearer $accessToken")
    }
}
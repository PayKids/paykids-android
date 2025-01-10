package com.paykids.domain.usecase.incomeCategory

import com.paykids.domain.repository.IncomeCategoryRepository
import javax.inject.Inject

class AddIncomeCategoryUseCase @Inject constructor(private val repository: IncomeCategoryRepository) {
    suspend operator fun invoke(accessToken: String, category: String): Result<Boolean> {
        return repository.saveIncomeCategory("Bearer $accessToken", category)
    }
}
package com.paykids.domain.usecase.incomeCategory

import com.paykids.domain.model.allowanceCategory.CategoryInfo
import com.paykids.domain.repository.IncomeCategoryRepository
import javax.inject.Inject

class GetIncomeCategoryUseCase @Inject constructor(private val repository: IncomeCategoryRepository) {
    suspend operator fun invoke(accessToken: String): Result<List<CategoryInfo>> {
        return repository.getIncomeCategoryList("Bearer $accessToken")
    }
}
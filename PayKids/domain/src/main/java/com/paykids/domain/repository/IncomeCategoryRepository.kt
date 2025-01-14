package com.paykids.domain.repository

import com.paykids.domain.model.allowanceCategory.CategoryInfo

interface IncomeCategoryRepository {

    suspend fun getIncomeCategoryList(
        accessToken: String,
    ): Result<List<CategoryInfo>>

    suspend fun addIncomeCategory(
        accessToken: String,
        category: String
    ): Result<Boolean>

    suspend fun deleteIncomeCategory(
        accessToken: String,
        category: String
    ): Result<Boolean>

}
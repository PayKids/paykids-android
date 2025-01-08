package com.paykids.domain.repository

import com.paykids.domain.model.allowanceCategory.CategoryInfo

interface ExpenseCategoryRepository {

    suspend fun getExpenseCategoryList(
        accessToken: String,
    ): Result<List<CategoryInfo>>

    suspend fun saveExpenseCategory(
        accessToken: String,
        category: String
    ): Result<Boolean>

    suspend fun deleteExpenseCategory(
        accessToken: String,
        category: String
    ): Result<Boolean>

}
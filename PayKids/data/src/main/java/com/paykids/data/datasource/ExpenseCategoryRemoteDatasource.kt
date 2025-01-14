package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowanceCategory.CategoryListDTO

interface ExpenseCategoryRemoteDatasource {

    suspend fun getExpenseCategoryList(
        accessToken: String,
    ): Result<BaseResponse<CategoryListDTO>>

    suspend fun addExpenseCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>>

    suspend fun deleteExpenseCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>>

}
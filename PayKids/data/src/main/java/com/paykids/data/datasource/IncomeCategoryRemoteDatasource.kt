package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowanceCategory.CategoryListDTO

interface IncomeCategoryRemoteDatasource {

    suspend fun getIncomeCategoryList(
        accessToken: String,
    ): Result<BaseResponse<CategoryListDTO>>

    suspend fun addIncomeCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>>

    suspend fun deleteIncomeCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>>

}
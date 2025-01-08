package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowance.MonthMostCategoryDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
import com.paykids.data.model.allowanceCategory.CategoryListDTO

interface ExpenseCategoryRemoteDatasource {

    suspend fun getExpenseCategoryList(
        accessToken: String,
    ): Result<BaseResponse<CategoryListDTO>>

    suspend fun saveExpenseCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>>

    suspend fun deleteExpenseCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>>

}
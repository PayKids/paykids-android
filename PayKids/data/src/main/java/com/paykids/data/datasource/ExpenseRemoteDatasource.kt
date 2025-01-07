package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowance.MonthMostCategoryDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO

interface ExpenseRemoteDatasource {
    suspend fun getMonthTotalExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<Int>>

    suspend fun getMonthMostExpenseCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthMostCategoryDTO>>

    suspend fun getMonthDailyExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthDailyDTO>>

    suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<BaseResponse<MonthCategoryDTO>>

    suspend fun getMonthAllCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthAllCategoryDTO>>

    suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayDTO>>

    suspend fun saveExpense(
        accessToken: String,
        expenseInfo: AddExpenseRequestDTO
    ): Result<BaseResponse<Boolean>>

    suspend fun updateExpense(
        accessToken: String,
        newExpenseInfo: UpdateExpenseRequestDTO
    ): Result<BaseResponse<Boolean>>

    suspend fun deleteExpense(
        id: Int, accessToken: String
    ): Result<BaseResponse<Boolean>>

}
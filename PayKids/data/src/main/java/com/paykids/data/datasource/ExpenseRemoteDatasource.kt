package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayExpenseResponseDTO
import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO

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
    ): Result<BaseResponse<String>>

    suspend fun getMonthDailyExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthDailyExpenseDTO>>

    suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<BaseResponse<UserInfoResponseDTO>>

    suspend fun getMonthAllCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<UserInfoResponseDTO>>

    suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayExpenseResponseDTO>>
}
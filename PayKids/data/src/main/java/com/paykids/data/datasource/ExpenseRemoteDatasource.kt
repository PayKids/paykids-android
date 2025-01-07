package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayExpenseDTO
import com.paykids.data.model.expense.ExpenseRequestDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryExpenseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.data.model.expense.MonthMostCategoryDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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
    ): Result<BaseResponse<MonthDailyExpenseDTO>>

    suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<BaseResponse<MonthCategoryExpenseDTO>>

    suspend fun getMonthAllCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthAllCategoryDTO>>

    suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayExpenseDTO>>

    suspend fun saveExpense(
        accessToken: String,
        expenseInfo: ExpenseRequestDTO
    ): Result<BaseResponse<Boolean>>

    suspend fun updateExpense(
        accessToken: String,
        newExpenseInfo: ExpenseRequestDTO
    ): Result<BaseResponse<Boolean>>

    suspend fun deleteExpense(
        id: Int, accessToken: String
    ): Result<BaseResponse<Boolean>>

}
package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayDTO
import com.paykids.data.model.expense.AddExpenseRequestDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryDTO
import com.paykids.data.model.expense.MonthDailyDTO
import com.paykids.data.model.expense.MonthMostCategoryDTO
import com.paykids.data.model.expense.UpdateExpenseRequestDTO

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

    suspend fun getMonthAllCategory(
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
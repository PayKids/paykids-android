package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayDTO
import com.paykids.data.model.expense.AddExpenseRequestDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryDTO
import com.paykids.data.model.expense.MonthDailyDTO
import com.paykids.data.model.expense.UpdateExpenseRequestDTO

interface IncomeRemoteDatasource {
    suspend fun getMonthTotalIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<Int>>

    suspend fun getMonthDailyIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthDailyDTO>>

    suspend fun getMonthCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<BaseResponse<MonthCategoryDTO>>

    suspend fun getMonthAllCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthAllCategoryDTO>>

    suspend fun getDayIncome(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayDTO>>

    suspend fun saveIncome(
        accessToken: String,
        expenseInfo: AddExpenseRequestDTO
    ): Result<BaseResponse<Boolean>>

    suspend fun updateIncome(
        accessToken: String,
        newExpenseInfo: UpdateExpenseRequestDTO
    ): Result<BaseResponse<Boolean>>

    suspend fun deleteIncome(
        id: Int, accessToken: String
    ): Result<BaseResponse<Boolean>>

}
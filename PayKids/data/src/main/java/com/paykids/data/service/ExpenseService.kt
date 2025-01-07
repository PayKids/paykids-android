package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayExpenseDTO
import com.paykids.data.model.expense.AddExpenseRequestDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryExpenseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.data.model.expense.MonthMostCategoryDTO
import com.paykids.data.model.expense.UpdateExpenseRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ExpenseService {

    @GET("/expense/allowance/month-total-amount")
    suspend fun getMonthTotalExpense(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<Int>>

    @GET("/expense/allowance/month-most-category")
    suspend fun getMonthMostExpenseCategory(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<MonthMostCategoryDTO>>

    @GET("/expense/allowance/month-daily-amount")
    suspend fun getMonthDailyExpense(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<MonthDailyExpenseDTO>>

    @GET("/expense/allowance/month-category")
    suspend fun getMonthCategoryExpense(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("category") category: String
    ): Response<BaseResponse<MonthCategoryExpenseDTO>>

    @GET("/expense/allowance/month-all-category")
    suspend fun getMonthAllCategory(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<BaseResponse<MonthAllCategoryDTO>>

    @GET("/expense/allowance/day")
    suspend fun getDayExpense(
        @Header("Authorization") accessToken: String,
        @Query("localDate") localDate: String
    ): Response<BaseResponse<DayExpenseDTO>>

    @POST("/expense/allowance/save")
    suspend fun saveExpense(
        @Header("Authorization") accessToken: String,
        @Body expenseInfo: AddExpenseRequestDTO
    ): Response<BaseResponse<Boolean>>

    @POST("/expense/allowance/replace")
    suspend fun updateExpense(
        @Header("Authorization") accessToken: String,
        @Body newExpenseInfo: UpdateExpenseRequestDTO
    ): Response<BaseResponse<Boolean>>

    @DELETE("/expense/allowance/delete")
    suspend fun deleteExpense(
        @Query("id") id: Int,
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<Boolean>>

}
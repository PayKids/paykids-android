package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayExpenseResponseDTO
import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.data.model.expense.MonthMostCategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
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
    ): Response<BaseResponse<UserInfoResponseDTO>>

    @GET("/expense/allowance/month-all-category")
    suspend fun getMonthAllCategory(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<BaseResponse<UserInfoResponseDTO>>

    @GET("/expense/allowance/day")
    suspend fun getDayExpense(
        @Header("Authorization") accessToken: String,
        @Query("localDate") localDate: String
    ): Response<BaseResponse<DayExpenseResponseDTO>>

}
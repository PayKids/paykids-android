package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IncomeService {

    @GET("/income/allowance/month-total-amount")
    suspend fun getMonthTotalIncome(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<Int>>

    @GET("/income/allowance/month-daily-amount")
    suspend fun getMonthDailyIncome(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<MonthDailyDTO>>

    @GET("/income/allowance/month-category")
    suspend fun getMonthCategoryIncome(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("category") category: String
    ): Response<BaseResponse<MonthCategoryDTO>>

    @GET("/income/allowance/month-all-category")
    suspend fun getMonthAllCategoryIncome(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<BaseResponse<MonthAllCategoryDTO>>

    @GET("/expense/allowance/day")
    suspend fun getDayIncome(
        @Header("Authorization") accessToken: String,
        @Query("localDate") localDate: String
    ): Response<BaseResponse<DayDTO>>

    @POST("/income/allowance/save")
    suspend fun saveIncome(
        @Header("Authorization") accessToken: String,
        @Body expenseInfo: AddExpenseRequestDTO
    ): Response<BaseResponse<Boolean>>

    @POST("/income/allowance/replace")
    suspend fun updateIncome(
        @Header("Authorization") accessToken: String,
        @Body newExpenseInfo: UpdateExpenseRequestDTO
    ): Response<BaseResponse<Boolean>>

    @DELETE("/income/allowance/delete")
    suspend fun deleteIncome(
        @Query("id") id: Int,
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<Boolean>>

}
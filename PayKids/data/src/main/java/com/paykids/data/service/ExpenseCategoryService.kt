package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowanceCategory.CategoryListDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ExpenseCategoryService {

    @GET("/expense/category/category-list")
    suspend fun getExpenseCategoryList(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<CategoryListDTO>>

    @POST("/expense/category/save-category")
    suspend fun addExpenseCategory(
        @Header("Authorization") accessToken: String,
        @Query("category") category: String,
    ): Response<BaseResponse<Boolean>>

    @DELETE("/expense/category/delete-category")
    suspend fun deleteExpenseCategory(
        @Header("Authorization") accessToken: String,
        @Query("category") category: String,
    ): Response<BaseResponse<Boolean>>

}
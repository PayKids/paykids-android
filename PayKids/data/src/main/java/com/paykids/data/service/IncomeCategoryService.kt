package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowanceCategory.CategoryListDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IncomeCategoryService {

    @GET("/income/category/category-list")
    suspend fun getIncomeCategoryList(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<CategoryListDTO>>

    @POST("/income/category/save-category")
    suspend fun saveIncomeCategory(
        @Header("Authorization") accessToken: String,
        @Query("category") category: String,
    ): Response<BaseResponse<Boolean>>

    @DELETE("/income/category/delete-category")
    suspend fun deleteIncomeCategory(
        @Header("Authorization") accessToken: String,
        @Query("category") category: String,
    ): Response<BaseResponse<Boolean>>

}
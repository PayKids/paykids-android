package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.model.UserTokenResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/auth/login")
    suspend fun signIn(
        @Query("idToken") idToken: String,
    ): Response<BaseResponse<UserTokenResponseDTO>>

    @POST("/auth/refresh")
    suspend fun getRefreshToken(
        @Query("refreshToken") refreshToken: String,
    ): Response<BaseResponse<UserTokenResponseDTO>>

}
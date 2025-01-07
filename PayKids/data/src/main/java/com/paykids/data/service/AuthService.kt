package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserTokenResponseDTO
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun signIn(
        @Header("Authorization") idToken: String,
    ): Response<BaseResponse<UserTokenResponseDTO>>

    @POST("/auth/refresh")
    suspend fun getRefreshToken(
        @Header("Authorization") refreshToken: String,
    ): Response<BaseResponse<UserTokenResponseDTO>>

}
package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserTokenResponseDTO

interface AuthRemoteDatasource {
    suspend fun signIn(idToken: String): Result<BaseResponse<UserTokenResponseDTO>>

    suspend fun getRefreshToken(refreshToken: String): Result<BaseResponse<UserTokenResponseDTO>>
}
package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.AuthRemoteDatasource
import com.paykids.data.datasource.ChatRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.ChatResponseDTO
import com.paykids.data.model.UserTokenResponseDTO
import com.paykids.data.service.AuthService
import com.paykids.data.service.ChatService
import javax.inject.Inject

class AuthRemoteDatasourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDatasource {
    override suspend fun getAccessToken(idToken: String): Result<BaseResponse<UserTokenResponseDTO>> {
        return try {
            val response = authService.getAccessToken(idToken)
            if (response.isSuccessful) {
                val chatResponse = response.body()

                if (chatResponse != null) {
                    Result.success(chatResponse)
                } else {
                    Result.failure(Exception("getAccessToken failed: response body is null"))
                }
            } else {
                Result.failure(Exception("getAccessToken failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRefreshToken(refreshToken: String): Result<BaseResponse<UserTokenResponseDTO>> {
        return try {
            val response = authService.getRefreshToken(refreshToken)
            if (response.isSuccessful) {
                val chatResponse = response.body()

                if (chatResponse != null) {
                    Result.success(chatResponse)
                } else {
                    Result.failure(Exception("getRefreshToken failed: response body is null"))
                }
            } else {
                Result.failure(Exception("getRefreshToken failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
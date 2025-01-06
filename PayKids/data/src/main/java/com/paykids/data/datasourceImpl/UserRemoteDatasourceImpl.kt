package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.UserRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.service.UserService
import javax.inject.Inject

class UserRemoteDatasourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDatasource {
    override suspend fun getUserInfo(accessToken: String): Result<BaseResponse<UserInfoResponseDTO>> {
        return try {
            val response = userService.getUserInfo(accessToken)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get UserInfo failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get UserInfo failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeProfileImage(accessToken: String): Result<BaseResponse<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNickname(accessToken: String, nickname: String): Result<BaseResponse<String>> {
        return try {
            val response = userService.saveNickname(accessToken, nickname)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("save Nickname failed: response body is null"))
                }
            } else {
                Result.failure(Exception("save Nickname failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeNickname(accessToken: String, newNickname: String): Result<BaseResponse<String>> {
        return try {
            val response = userService.updateNickname(accessToken, newNickname)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("change Nickname failed: response body is null"))
                }
            } else {
                Result.failure(Exception("change Nickname failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
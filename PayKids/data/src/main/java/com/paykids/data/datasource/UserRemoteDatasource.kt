package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserInfoResponseDTO
import okhttp3.MultipartBody

interface UserRemoteDatasource {
    suspend fun getUserInfo(accessToken: String): Result<BaseResponse<UserInfoResponseDTO>>

    suspend fun updateProfileImage(accessToken: String, file: MultipartBody.Part): Result<BaseResponse<String>>

    suspend fun saveNickname(accessToken: String, nickname: String): Result<BaseResponse<String>>

    suspend fun changeNickname(accessToken: String, newNickname: String): Result<BaseResponse<String>>

    suspend fun deleteUser(accessToken: String): Result<BaseResponse<String>>
}
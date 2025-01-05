package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.UserInfoResponseDTO

interface UserRemoteDatasource {
    suspend fun getUserInfo(accessToken: String): Result<BaseResponse<UserInfoResponseDTO>>

    suspend fun changeProfileImage(accessToken: String): Result<BaseResponse<String>>

    suspend fun saveNickname(accessToken: String, nickname: String): Result<BaseResponse<String>>

    suspend fun changeNickname(accessToken: String, newNickname: String): Result<BaseResponse<String>>
}
package com.paykids.data.datasource

import com.kakao.sdk.user.model.User
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.ChatResponseDTO
import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.model.UserTokenResponseDTO

interface UserRemoteDatasource {
    suspend fun getUserInfo(accessToken: String): Result<BaseResponse<UserInfoResponseDTO>>

    suspend fun changeProfileImage(accessToken: String): Result<BaseResponse<String>>

    suspend fun saveNickname(accessToken: String, nickname: String): Result<BaseResponse<String>>

    suspend fun changeNickname(accessToken: String, newNickname: String): Result<BaseResponse<String>>
}
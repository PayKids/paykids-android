package com.paykids.domain.repository

import com.paykids.domain.model.auth.UserSignInInfo
import com.paykids.domain.model.user.UserInfo

interface UserRepository {
    suspend fun getUserInfo(accessToken: String): Result<UserInfo>

    suspend fun changeProfileImage(accessToken: String): Result<String>

    suspend fun saveNickname(accessToken: String): Result<String>

    suspend fun changeNickname(accessToken: String): Result<String>
}
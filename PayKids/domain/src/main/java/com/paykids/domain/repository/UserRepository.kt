package com.paykids.domain.repository

import com.paykids.domain.model.user.UserInfo
import java.io.File

interface UserRepository {
    suspend fun getUserInfo(accessToken: String): Result<UserInfo>

    suspend fun updateProfileImage(
        accessToken: String,
        imageFile: File,
        mimeType: String = "image/*"
    ): Result<String>

    suspend fun saveNickname(accessToken: String, nickname: String): Result<String>

    suspend fun changeNickname(accessToken: String, newNickname: String): Result<String>
}
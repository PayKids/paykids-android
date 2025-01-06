package com.paykids.data.repository

import com.paykids.data.datasource.UserRemoteDatasource
import com.paykids.data.mapper.UserMapper
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.UserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDatasource: UserRemoteDatasource
) : UserRepository {
    override suspend fun getUserInfo(accessToken: String): Result<UserInfo> {
        val result = userDatasource.getUserInfo(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(UserMapper.mapperToResponseEntity(data))
            } else {
                Result.failure(Exception("get UserInfo Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun updateProfileImage(
        accessToken: String,
        file: MultipartBody.Part
    ): Result<String> {
        val result = userDatasource.updateProfileImage("Bearer $accessToken", file)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("업로드 실패: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun saveNickname(accessToken: String, nickname: String): Result<String> {
        val result = userDatasource.saveNickname(accessToken, nickname)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("save Nickname Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun changeNickname(accessToken: String, newNickname: String): Result<String> {
        val result = userDatasource.changeNickname(accessToken, newNickname)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("change Nickname Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

}
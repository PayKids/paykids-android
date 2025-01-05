package com.paykids.data.repository

import com.paykids.data.datasource.UserRemoteDatasource
import com.paykids.data.mapper.UserMapper
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.UserRepository
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

    override suspend fun changeProfileImage(accessToken: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNickname(accessToken: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun changeNickname(accessToken: String): Result<String> {
        TODO("Not yet implemented")
    }

}
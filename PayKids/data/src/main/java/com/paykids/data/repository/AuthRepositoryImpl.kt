package com.paykids.data.repository

import com.paykids.data.datasource.AuthRemoteDatasource
import com.paykids.data.mapper.SignMapper
import com.paykids.domain.model.auth.UserSignInInfo
import com.paykids.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDatasource: AuthRemoteDatasource,
) : AuthRepository {

    override suspend fun signIn(idToken: String): Result<UserSignInInfo> {
        val result = authRemoteDatasource.signIn(idToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(SignMapper.mapperToResponseEntity(data))
            } else {
                Result.failure(Exception("Sign In Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
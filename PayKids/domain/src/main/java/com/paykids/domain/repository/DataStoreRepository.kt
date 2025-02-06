package com.paykids.domain.repository

interface DataStoreRepository {

    suspend fun clearData(): Result<Boolean>

    suspend fun clearUserData(): Result<Boolean>

    suspend fun setAccessToken(
        accessToken: String
    ): Result<Boolean>

    suspend fun getAccessToken(): Result<String>

    suspend fun setRefreshToken(
        refreshToken: String
    ): Result<Boolean>

    suspend fun getRefreshToken(): Result<String>
}
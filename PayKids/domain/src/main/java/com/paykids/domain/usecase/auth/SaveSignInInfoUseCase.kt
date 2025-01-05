package com.paykids.domain.usecase.auth

import com.paykids.domain.repository.DataStoreRepository

class SaveSignInInfoUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(accessToken: String, refreshToken: String): Result<Boolean> {
        val step1 = repository.setAccessToken(accessToken).isSuccess
        val step2 = repository.setRefreshToken(refreshToken).isSuccess

        return if(step1 && step2) Result.success(true) else Result.success(false)
    }
}
package com.paykids.domain.usecase.datastore

import com.paykids.domain.repository.DataStoreRepository

class GetAccessTokenUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(): Result<String> {
        return repository.getAccessToken()
    }
}
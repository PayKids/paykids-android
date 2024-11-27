package com.paykids.domain.usecase.datastore

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.repository.DataStoreRepository

class GetAuthProviderUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(): Result<AuthProvider> {
        return repository.getAuthProvider()
    }
}
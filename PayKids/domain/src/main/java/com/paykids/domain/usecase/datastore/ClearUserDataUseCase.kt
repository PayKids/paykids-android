package com.paykids.domain.usecase.datastore

import com.paykids.domain.repository.DataStoreRepository

class ClearUserDataUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(): Result<Boolean> {
        return repository.clearUserData()
    }
}
package com.paykids.domain.usecase.auth

import com.paykids.domain.repository.AuthRepository
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(accessToken: String): Result<String> {
        return repository.withdraw(accessToken)
    }
}
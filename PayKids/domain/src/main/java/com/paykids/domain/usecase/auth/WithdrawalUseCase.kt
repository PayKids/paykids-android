package com.paykids.domain.usecase.auth

import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(private val repository: KakaoAuthRepository) {
    suspend operator fun invoke(accessToken: String): Result<String> {
        return repository.withdraw(accessToken)
    }
}
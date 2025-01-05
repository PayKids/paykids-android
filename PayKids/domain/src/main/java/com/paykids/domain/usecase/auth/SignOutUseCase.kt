package com.paykids.domain.usecase.auth

import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: KakaoAuthRepository) {
    suspend operator fun invoke(accessToken: String): Result<Boolean> {
        return repository.signOut(accessToken)
    }
}
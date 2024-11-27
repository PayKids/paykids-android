package com.paykids.domain.usecase.auth

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(idToken: String, provider: AuthProvider): Result<SignInInfo> {
        return repository.signIn(idToken, provider)
    }
}
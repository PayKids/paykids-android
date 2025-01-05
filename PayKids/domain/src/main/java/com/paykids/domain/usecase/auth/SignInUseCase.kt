package com.paykids.domain.usecase.auth

import com.paykids.domain.model.auth.UserSignInInfo
import com.paykids.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(idToken: String): Result<UserSignInInfo> {
        return repository.signIn(idToken)
    }
}
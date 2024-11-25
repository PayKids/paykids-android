package com.paykids.data.usecase

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.SignInRepository
import com.paykids.domain.usecase.SignInUseCase
import javax.inject.Inject

class SignInUseCaseImpl @Inject constructor(
    private val repository: SignInRepository
) : SignInUseCase {

    override suspend fun invoke(idToken: String, provider: AuthProvider): Result<SignInInfo> {
        return repository.signIn(idToken, provider)
    }
}
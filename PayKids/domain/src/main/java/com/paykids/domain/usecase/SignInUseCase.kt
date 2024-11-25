package com.paykids.domain.usecase

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo

interface SignInUseCase {
    suspend operator fun invoke(idToken: String, provider: AuthProvider): Result<SignInInfo>
}
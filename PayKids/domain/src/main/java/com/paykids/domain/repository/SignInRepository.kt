package com.paykids.domain.repository

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo

interface SignInRepository {
    suspend fun signIn(idToken: String, provider: AuthProvider): Result<SignInInfo>
}
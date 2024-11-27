package com.paykids.domain.repository

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo

interface AuthRepository {
    suspend fun signIn(idToken: String, provider: AuthProvider): Result<SignInInfo>

    suspend fun signOut(accessToken: String): Result<Boolean>
}
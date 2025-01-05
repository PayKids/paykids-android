package com.paykids.domain.repository

import com.paykids.domain.model.auth.UserSignInInfo

interface AuthRepository {
    suspend fun signIn(idToken: String): Result<UserSignInInfo>
}
package com.paykids.domain.repository

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.model.UserSignInInfo

interface AuthRepository {
    suspend fun signIn(idToken: String): Result<UserSignInInfo>
}
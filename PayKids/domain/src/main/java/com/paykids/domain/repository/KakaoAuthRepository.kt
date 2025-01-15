package com.paykids.domain.repository

import com.paykids.domain.model.auth.SignInInfo

interface KakaoAuthRepository {
    suspend fun signInWithKakao(context: Any): Result<SignInInfo>

    suspend fun signOut(accessToken: String): Result<Boolean>

    suspend fun withdraw(accessToken: String): Result<String>
}
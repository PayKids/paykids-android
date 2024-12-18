package com.paykids.domain.repository

import com.paykids.domain.model.SignInInfo

interface KakaoAuthRepository {
    suspend fun signInWithKakao(): SignInInfo

    suspend fun signOut(accessToken: String): Result<Boolean>

    suspend fun withdraw(accessToken: String): Result<String>
}
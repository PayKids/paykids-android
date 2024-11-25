package com.paykids.domain.repository

import com.paykids.domain.model.SignInInfo

interface KakaoAuthRepository {
    suspend fun signInWithKakao(): SignInInfo
}
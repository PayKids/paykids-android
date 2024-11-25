package com.paykids.data.repository

import com.paykids.data.service.KakaoAuthService
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject


class KakaoAuthRepositoryImpl @Inject constructor(
    private val kakaoAuthService: KakaoAuthService
) : KakaoAuthRepository {
    override suspend fun signInWithKakao(): SignInInfo {
        return kakaoAuthService.signInWithKakao()
    }
}
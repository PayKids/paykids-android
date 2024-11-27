package com.paykids.data.service

import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject

class AuthService @Inject constructor(
    private val kakaoAuthRepository: KakaoAuthRepository
) {
//    suspend fun authenticate(provider: AuthProvider): SignInInfo {
//        return when (provider) {
//            AuthProvider.KAKAO -> kakaoAuthRepository.signInWithKakao()
//            else -> throw IllegalArgumentException("Unsupported provider: $provider")
//        }
//    }
}
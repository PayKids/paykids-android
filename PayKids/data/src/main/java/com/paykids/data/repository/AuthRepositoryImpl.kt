package com.paykids.data.repository

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val kakaoAuthRepository: KakaoAuthRepository,
) : AuthRepository {


    override suspend fun signIn(idToken: String, provider: AuthProvider): Result<SignInInfo> {
        if (idToken.isBlank()) {
            return Result.failure(IllegalArgumentException("idToken이 비어있습니다"))
        }

        return when (provider) {
            AuthProvider.KAKAO -> {
                try {
                    val signInInfo = kakaoAuthRepository.signInWithKakao() // KakaoAuth 사용
                    Result.success(signInInfo)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            else -> Result.failure(Exception("Unknown provider"))
        }
    }

    override suspend fun signOut(accessToken: String): Result<Boolean> {
        return try {
            kakaoAuthRepository.signOut(accessToken)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun withdraw(accessToken: String): Result<String> {
        return try {
            val res = kakaoAuthRepository.withdraw(accessToken)
            res
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
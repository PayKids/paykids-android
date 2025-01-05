package com.paykids.data.repository

import com.paykids.data.service.KakaoAuthService
import com.paykids.domain.model.auth.SignInInfo
import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject


class KakaoAuthRepositoryImpl @Inject constructor(
    private val kakaoAuthService: KakaoAuthService
) : KakaoAuthRepository {
    override suspend fun signInWithKakao(): Result<SignInInfo> {
        return try {
            val res = kakaoAuthService.signInWithKakao()
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(accessToken: String): Result<Boolean> {
        return try {
            kakaoAuthService.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun withdraw(accessToken: String): Result<String> {
        return try {
            val res = kakaoAuthService.withdraw()
            Result.success(res.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
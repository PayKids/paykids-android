package com.paykids.data.repository

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.repository.SignInRepository
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val kakaoAuthRepository: KakaoAuthRepository,
) : SignInRepository {

    override suspend fun signIn(idToken: String, provider: AuthProvider): Result<SignInInfo> {
        return when (provider) {
            AuthProvider.KAKAO -> {
                try {
                    val signInInfo = kakaoAuthRepository.signInWithKakao()

                    val email = signInInfo.email ?: "example@kakao.com"
                    val name = signInInfo.name ?: "John Doe"

                    Result.success(
                        SignInInfo(
                            idToken = idToken,
                            provider = AuthProvider.KAKAO,
                            email = email,
                            name = name
                        )
                    )

                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            else -> Result.failure(Exception("Unknown provider"))
        }
    }
}
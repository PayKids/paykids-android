package com.paykids.domain.usecase.auth

import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.KakaoAuthRepository
import javax.inject.Inject

class KakaoAuthUseCase @Inject constructor(private val repository: KakaoAuthRepository) {
    suspend operator fun invoke(): SignInInfo {
        return repository.signInWithKakao()
    }
}
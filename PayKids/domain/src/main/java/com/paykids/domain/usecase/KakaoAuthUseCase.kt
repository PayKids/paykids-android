package com.paykids.domain.usecase

import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.repository.SignInRepository
import javax.inject.Inject

class KakaoAuthUseCase @Inject constructor(
    private val repository: KakaoAuthRepository
) {
    suspend operator fun invoke(): SignInInfo {
        return repository.signInWithKakao()
    }
}
package com.paykids.data.di

import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.repository.SignInRepository
import com.paykids.domain.usecase.KakaoAuthUseCase
import com.paykids.domain.usecase.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoAuthUseCaseModule {

    @Provides
    fun provideKakaoAuthUseCase(
        kakaoAuthRepository: KakaoAuthRepository
    ): KakaoAuthUseCase {
        return KakaoAuthUseCase(kakaoAuthRepository)
    }
}


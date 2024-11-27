package com.paykids.data.di

import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.usecase.auth.KakaoAuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoAuthUseCaseModule {

    @Provides
    @Singleton
    fun provideKakaoAuthUseCase(
        kakaoAuthRepository: KakaoAuthRepository
    ): KakaoAuthUseCase {
        return KakaoAuthUseCase(kakaoAuthRepository)
    }
}


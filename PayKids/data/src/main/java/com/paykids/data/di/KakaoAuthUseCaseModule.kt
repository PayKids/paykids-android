package com.paykids.data.di

import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.usecase.auth.KakaoAuthUseCase
import com.paykids.domain.usecase.auth.SignOutUseCase
import com.paykids.domain.usecase.auth.WithdrawalUseCase
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
    fun provideSignInUseCase(
        repository: KakaoAuthRepository
    ): KakaoAuthUseCase {
        return KakaoAuthUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        kakaoAuthRepository: KakaoAuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(kakaoAuthRepository)
    }

    @Provides
    @Singleton
    fun provideWithdrawalUseCase(
        kakaoAuthRepository: KakaoAuthRepository
    ): WithdrawalUseCase {
        return WithdrawalUseCase(kakaoAuthRepository)
    }
}


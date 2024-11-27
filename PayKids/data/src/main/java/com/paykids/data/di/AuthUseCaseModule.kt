package com.paykids.data.di

import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.usecase.auth.SignInUseCase
import com.paykids.domain.usecase.auth.SignOutUseCase
import com.paykids.domain.usecase.auth.WithdrawalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    @Singleton
    fun provideSignInUseCase(
        repository: AuthRepository
    ): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authRepository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideWithdrawalUseCase(
        authRepository: AuthRepository
    ): WithdrawalUseCase {
        return WithdrawalUseCase(authRepository)
    }
}


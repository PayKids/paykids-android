package com.paykids.data.di

import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.usecase.auth.SignOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignOutUseCaseModule {

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authRepository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }
}


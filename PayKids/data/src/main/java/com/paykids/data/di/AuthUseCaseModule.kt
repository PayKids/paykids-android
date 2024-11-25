package com.paykids.data.di

import com.paykids.domain.repository.SignInRepository
import com.paykids.domain.usecase.SignInUseCase
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
        repository: SignInRepository
    ): SignInUseCase {
        return SignInUseCase(repository)
    }
}


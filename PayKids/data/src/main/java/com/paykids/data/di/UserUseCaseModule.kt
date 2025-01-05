package com.paykids.data.di

import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.DataStoreRepository
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.repository.UserRepository
import com.paykids.domain.usecase.auth.KakaoAuthUseCase
import com.paykids.domain.usecase.auth.SaveSignInInfoUseCase
import com.paykids.domain.usecase.auth.SignInUseCase
import com.paykids.domain.usecase.auth.SignOutUseCase
import com.paykids.domain.usecase.auth.WithdrawalUseCase
import com.paykids.domain.usecase.user.GetUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        repository: UserRepository
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository)
    }

}


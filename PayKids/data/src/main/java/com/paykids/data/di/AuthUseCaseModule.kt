package com.paykids.data.di

import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.DataStoreRepository
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.usecase.auth.KakaoAuthUseCase
import com.paykids.domain.usecase.auth.SaveSignInInfoUseCase
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
    fun provideSaveSignInInfoUseCase(
        repository: DataStoreRepository
    ): SaveSignInInfoUseCase {
        return SaveSignInInfoUseCase(repository)
    }

}


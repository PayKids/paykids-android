package com.paykids.data.di

import com.paykids.domain.repository.UserRepository
import com.paykids.domain.usecase.user.ChangeNicknameUseCase
import com.paykids.domain.usecase.user.GetUserInfoUseCase
import com.paykids.domain.usecase.user.SaveNicknameUseCase
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

    @Provides
    @Singleton
    fun provideSaveNicknameUseCase(
        repository: UserRepository
    ): SaveNicknameUseCase {
        return SaveNicknameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideChangeNicknameUseCase(
        repository: UserRepository
    ): ChangeNicknameUseCase {
        return ChangeNicknameUseCase(repository)
    }
}


package com.paykids.data.di

import com.paykids.domain.repository.DataStoreRepository
import com.paykids.domain.usecase.auth.SaveSignInInfoUseCase
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


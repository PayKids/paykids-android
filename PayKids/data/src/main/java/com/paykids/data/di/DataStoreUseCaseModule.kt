package com.paykids.data.di

import com.paykids.domain.repository.DataStoreRepository
import com.paykids.domain.usecase.datastore.ClearUserDataUseCase
import com.paykids.domain.usecase.datastore.GetAccessTokenUseCase
import com.paykids.domain.usecase.datastore.GetAuthProviderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAccessTokenUseCase(
        dataStoreRepository: DataStoreRepository
    ): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideGetAuthProviderUseCase(
        dataStoreRepository: DataStoreRepository
    ): GetAuthProviderUseCase {
        return GetAuthProviderUseCase(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideClearUserDataUseCase(
        dataStoreRepository: DataStoreRepository
    ): ClearUserDataUseCase {
        return ClearUserDataUseCase(dataStoreRepository)
    }
}
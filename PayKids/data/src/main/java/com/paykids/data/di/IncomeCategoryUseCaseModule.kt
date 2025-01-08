package com.paykids.data.di

import com.paykids.domain.repository.IncomeCategoryRepository
import com.paykids.domain.usecase.incomeCategory.DeleteIncomeCategoryUseCase
import com.paykids.domain.usecase.incomeCategory.SaveIncomeCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IncomeCategoryUseCaseModule {

    @Provides
    @Singleton
    fun provideSaveIncomeCategoryUseCase(
        repository: IncomeCategoryRepository
    ): SaveIncomeCategoryUseCase {
        return SaveIncomeCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteIncomeCategoryUseCase(
        repository: IncomeCategoryRepository
    ): DeleteIncomeCategoryUseCase {
        return DeleteIncomeCategoryUseCase(repository)
    }
}


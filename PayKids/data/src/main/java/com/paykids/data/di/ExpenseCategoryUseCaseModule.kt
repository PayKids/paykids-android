package com.paykids.data.di

import com.paykids.domain.repository.ExpenseCategoryRepository
import com.paykids.domain.usecase.expenseCategory.DeleteExpenseCategoryUseCase
import com.paykids.domain.usecase.expenseCategory.AddExpenseCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseCategoryUseCaseModule {

    @Provides
    @Singleton
    fun provideSaveExpenseCategoryUseCase(
        repository: ExpenseCategoryRepository
    ): AddExpenseCategoryUseCase {
        return AddExpenseCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteExpenseCategoryUseCase(
        repository: ExpenseCategoryRepository
    ): DeleteExpenseCategoryUseCase {
        return DeleteExpenseCategoryUseCase(repository)
    }
}


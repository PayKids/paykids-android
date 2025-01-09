package com.paykids.data.di

import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.usecase.expense.AddExpenseUseCase
import com.paykids.domain.usecase.expense.GetDayExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthAllCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthDailyExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthMostCategoryUseCase
import com.paykids.domain.usecase.expense.GetMonthTotalExpenseUseCase
import com.paykids.domain.usecase.expense.UpdateExpenseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseUseCaseModule {

    @Provides
    @Singleton
    fun provideGetMonthTotalExpenseUseCase(
        repository: ExpenseRepository
    ): GetMonthTotalExpenseUseCase {
        return GetMonthTotalExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthMostCategoryUseCase(
        repository: ExpenseRepository
    ): GetMonthMostCategoryUseCase {
        return GetMonthMostCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthDailyExpenseUseCase(
        repository: ExpenseRepository
    ): GetMonthDailyExpenseUseCase {
        return GetMonthDailyExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthCategoryExpenseUseCase(
        repository: ExpenseRepository
    ): GetMonthCategoryExpenseUseCase {
        return GetMonthCategoryExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthAllCategoryUseCase(
        repository: ExpenseRepository
    ): GetMonthAllCategoryExpenseUseCase {
        return GetMonthAllCategoryExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetDayExpenseUseCase(
        repository: ExpenseRepository
    ): GetDayExpenseUseCase {
        return GetDayExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddExpenseUseCase(
        repository: ExpenseRepository
    ): AddExpenseUseCase {
        return AddExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateExpenseUseCase(
        repository: ExpenseRepository
    ): UpdateExpenseUseCase {
        return UpdateExpenseUseCase(repository)
    }
}


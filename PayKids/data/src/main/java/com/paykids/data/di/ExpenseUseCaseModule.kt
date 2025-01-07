package com.paykids.data.di

import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.UserRepository
import com.paykids.domain.usecase.expense.GetDayExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthAllCategoryUseCase
import com.paykids.domain.usecase.expense.GetMonthCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthDailyExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthMostCategoryUseCase
import com.paykids.domain.usecase.expense.GetMonthTotalExpenseUseCase
import com.paykids.domain.usecase.user.ChangeNicknameUseCase
import com.paykids.domain.usecase.user.GetUserInfoUseCase
import com.paykids.domain.usecase.user.SaveNicknameUseCase
import com.paykids.domain.usecase.user.UpdateProfileImageUseCase
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
    ): GetMonthAllCategoryUseCase {
        return GetMonthAllCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetDayExpenseUseCase(
        repository: ExpenseRepository
    ): GetDayExpenseUseCase {
        return GetDayExpenseUseCase(repository)
    }

}


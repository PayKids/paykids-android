package com.paykids.data.di

import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.UserRepository
import com.paykids.domain.usecase.expense.GetMonthDailyExpenseUseCase
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
    fun provideGetMonthDailyExpenseUseCaseUseCase(
        repository: ExpenseRepository
    ): GetMonthDailyExpenseUseCase {
        return GetMonthDailyExpenseUseCase(repository)
    }

}


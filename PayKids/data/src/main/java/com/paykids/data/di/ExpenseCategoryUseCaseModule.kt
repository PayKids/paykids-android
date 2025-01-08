package com.paykids.data.di

import com.paykids.domain.repository.ExpenseCategoryRepository
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.usecase.expense.AddExpenseUseCase
import com.paykids.domain.usecase.expense.GetDayExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthAllCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthCategoryExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthDailyExpenseUseCase
import com.paykids.domain.usecase.expense.GetMonthMostCategoryUseCase
import com.paykids.domain.usecase.expense.GetMonthTotalExpenseUseCase
import com.paykids.domain.usecase.expense.UpdateExpenseUseCase
import com.paykids.domain.usecase.expenseCategory.DeleteExpenseCategoryUseCase
import com.paykids.domain.usecase.expenseCategory.SaveExpenseCategoryUseCase
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
    ): SaveExpenseCategoryUseCase {
        return SaveExpenseCategoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteExpenseCategoryUseCase(
        repository: ExpenseCategoryRepository
    ): DeleteExpenseCategoryUseCase {
        return DeleteExpenseCategoryUseCase(repository)
    }
}


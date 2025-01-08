package com.paykids.data.di

import com.paykids.domain.repository.ExpenseCategoryRepository
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.IncomeCategoryRepository
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


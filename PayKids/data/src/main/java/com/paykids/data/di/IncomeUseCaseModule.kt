package com.paykids.data.di

import com.paykids.domain.repository.IncomeRepository
import com.paykids.domain.usecase.income.AddIncomeUseCase
import com.paykids.domain.usecase.income.GetDayIncomeUseCase
import com.paykids.domain.usecase.income.GetMonthAllCategoryIncomeUseCase
import com.paykids.domain.usecase.income.GetMonthCategoryIncomeUseCase
import com.paykids.domain.usecase.income.GetMonthDailyIncomeUseCase
import com.paykids.domain.usecase.income.GetMonthTotalIncomeUseCase
import com.paykids.domain.usecase.income.UpdateIncomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IncomeUseCaseModule {

    @Provides
    @Singleton
    fun provideGetMonthTotalIncomeUseCase(
        repository: IncomeRepository
    ): GetMonthTotalIncomeUseCase {
        return GetMonthTotalIncomeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthDailyIncomeUseCase(
        repository: IncomeRepository
    ): GetMonthDailyIncomeUseCase {
        return GetMonthDailyIncomeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthCategoryIncomeUseCase(
        repository: IncomeRepository
    ): GetMonthCategoryIncomeUseCase {
        return GetMonthCategoryIncomeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthAllCategoryIncomeUseCase(
        repository: IncomeRepository
    ): GetMonthAllCategoryIncomeUseCase {
        return GetMonthAllCategoryIncomeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetDayIncomeUseCase(
        repository: IncomeRepository
    ): GetDayIncomeUseCase {
        return GetDayIncomeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddIncomeUseCase(
        repository: IncomeRepository
    ): AddIncomeUseCase {
        return AddIncomeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateIncomeUseCase(
        repository: IncomeRepository
    ): UpdateIncomeUseCase {
        return UpdateIncomeUseCase(repository)
    }

}


package com.paykids.data.di

import com.paykids.data.repository.AuthRepositoryImpl
import com.paykids.data.repository.ChatRepositoryImpl
import com.paykids.data.repository.ExpenseCategoryRepositoryImpl
import com.paykids.data.repository.ExpenseRepositoryImpl
import com.paykids.data.repository.IncomeCategoryRepositoryImpl
import com.paykids.data.repository.IncomeRepositoryImpl
import com.paykids.data.repository.KakaoAuthRepositoryImpl
import com.paykids.data.repository.QuizRepositoryImpl
import com.paykids.data.repository.UserRepositoryImpl
import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.ChatRepository
import com.paykids.domain.repository.ExpenseCategoryRepository
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.IncomeCategoryRepository
import com.paykids.domain.repository.IncomeRepository
import com.paykids.domain.repository.KakaoAuthRepository
import com.paykids.domain.repository.QuizRepository
import com.paykids.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsKakaoAuthRepository(impl: KakaoAuthRepositoryImpl): KakaoAuthRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindsChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindsQuizRepository(impl: QuizRepositoryImpl): QuizRepository
    abstract fun bindsExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindsIncomeRepository(impl: IncomeRepositoryImpl): IncomeRepository

    @Binds
    @Singleton
    abstract fun bindsExpenseCategoryRepository(impl: ExpenseCategoryRepositoryImpl): ExpenseCategoryRepository

    @Binds
    @Singleton
    abstract fun bindsIncomeCategoryRepository(impl: IncomeCategoryRepositoryImpl): IncomeCategoryRepository
}
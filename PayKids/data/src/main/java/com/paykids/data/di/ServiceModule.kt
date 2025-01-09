package com.paykids.data.di

import com.paykids.data.service.AuthService
import com.paykids.data.service.ChatService
import com.paykids.data.service.QuizService
import com.paykids.data.service.ExpenseCategoryService
import com.paykids.data.service.ExpenseService
import com.paykids.data.service.IncomeCategoryService
import com.paykids.data.service.IncomeService
import com.paykids.data.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesChatService(retrofit: Retrofit): ChatService =
        retrofit.create(ChatService::class.java)

    @Provides
    @Singleton
    fun providesQuizService(retrofit: Retrofit): QuizService =
        retrofit.create(QuizService::class.java)
    fun providesExpenseService(retrofit: Retrofit): ExpenseService =
        retrofit.create(ExpenseService::class.java)

    @Provides
    @Singleton
    fun providesIncomeService(retrofit: Retrofit): IncomeService =
        retrofit.create(IncomeService::class.java)

    @Provides
    @Singleton
    fun providesExpenseCategoryService(retrofit: Retrofit): ExpenseCategoryService =
        retrofit.create(ExpenseCategoryService::class.java)

    @Provides
    @Singleton
    fun providesIncomeCategoryService(retrofit: Retrofit): IncomeCategoryService =
        retrofit.create(IncomeCategoryService::class.java)

}
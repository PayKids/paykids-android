package com.paykids.data.di

import com.paykids.domain.repository.QuizRepository
import com.paykids.domain.usecase.quiz.GetStageNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizUseCaseModule {

    @Provides
    @Singleton
    fun provideGetStageNameUseCase(
        repository: QuizRepository
    ): GetStageNameUseCase {
        return GetStageNameUseCase(repository)
    }
}
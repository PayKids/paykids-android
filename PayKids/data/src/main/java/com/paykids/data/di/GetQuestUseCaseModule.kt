package com.paykids.data.di

import com.paykids.domain.repository.QuestRepository
import com.paykids.domain.usecase.quest.GetQuestUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GetQuestUseCaseModule {

    @Provides
    @Singleton
    fun provideGetQuestUseCase(
        repository: QuestRepository
    ): GetQuestUseCase {
        return GetQuestUseCase(repository)
    }
}


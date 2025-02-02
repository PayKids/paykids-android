package com.paykids.data.di

import com.paykids.domain.repository.AchievementRepository
import com.paykids.domain.repository.ChatRepository
import com.paykids.domain.usecase.acievement.GetAchievementUseCase
import com.paykids.domain.usecase.chat.SendChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GetAchievementUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAchievementUseCase(
        repository: AchievementRepository
    ): GetAchievementUseCase {
        return GetAchievementUseCase(repository)
    }
}


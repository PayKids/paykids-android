package com.paykids.data.di

import com.paykids.domain.repository.ChatRepository
import com.paykids.domain.usecase.chat.SendChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ChatUseCaseModule {

    @Provides
    @Singleton
    fun provideSendChatUseCase(
        chatRepository: ChatRepository
    ): SendChatUseCase {
        return SendChatUseCase(chatRepository)
    }
}


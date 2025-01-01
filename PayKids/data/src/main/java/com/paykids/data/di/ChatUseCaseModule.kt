package com.paykids.data.di

import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.ChatRepository
import com.paykids.domain.usecase.auth.SignInUseCase
import com.paykids.domain.usecase.auth.SignOutUseCase
import com.paykids.domain.usecase.auth.WithdrawalUseCase
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


package com.paykids.domain.usecase.chat

import com.paykids.domain.repository.ChatRepository

class SendChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(question: String): Result<String> {
        return repository.sendChat(question)
    }
}
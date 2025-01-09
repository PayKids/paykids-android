package com.paykids.domain.usecase.chat

import com.paykids.domain.repository.ChatRepository

class SendChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(accessToken: String, question: String): Result<String> {
        return repository.sendChat("Bearer $accessToken", question)
    }
}
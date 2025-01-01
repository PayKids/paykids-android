package com.paykids.data.datasource

import com.paykids.data.model.ChatResponseDTO

interface ChatRemoteDatasource {
    suspend fun sendChat(prompt: String): Result<ChatResponseDTO>
}
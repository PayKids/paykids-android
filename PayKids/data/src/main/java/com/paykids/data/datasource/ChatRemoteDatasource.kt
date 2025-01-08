package com.paykids.data.datasource

import com.paykids.data.model.ChatResponseDTO

interface ChatRemoteDatasource {
    suspend fun sendChat(accessToken: String, prompt: String): Result<ChatResponseDTO>
}
package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.ChatRemoteDatasource
import com.paykids.data.model.ChatResponseDTO
import com.paykids.data.service.ChatService
import javax.inject.Inject

class ChatRemoteDatasourceImpl @Inject constructor(
    private val chatService: ChatService
) : ChatRemoteDatasource {
    override suspend fun sendChat(accessToken: String, prompt: String): Result<ChatResponseDTO> {
        return try {
            val response = chatService.sendChat(accessToken, prompt)
            if (response.isSuccessful) {
                val chatResponse = response.body()

                if (chatResponse != null) {
                    Result.success(chatResponse)
                } else {
                    Result.failure(Exception("SendChat failed: response body is null"))
                }
            } else {
                Result.failure(Exception("SendChat failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
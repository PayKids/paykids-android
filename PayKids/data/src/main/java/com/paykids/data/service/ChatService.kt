package com.paykids.data.service

import com.paykids.data.model.ChatResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ChatService {

    @GET("/gpt/number")
    suspend fun getChatNumber(
        @Header("Authorization") accessToken: String,
    ):  Response<ChatResponseDTO>

    @GET("/gpt/chat")
    suspend fun sendChat(
        @Header("Authorization") accessToken: String,
        @Query("prompt") prompt: String
    ):  Response<ChatResponseDTO>

}
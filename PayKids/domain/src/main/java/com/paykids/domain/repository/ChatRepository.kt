package com.paykids.domain.repository

interface ChatRepository {
    suspend fun sendChat(accessToken: String, question: String): Result<String>
}
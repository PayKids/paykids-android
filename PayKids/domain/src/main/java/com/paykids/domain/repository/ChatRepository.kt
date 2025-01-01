package com.paykids.domain.repository

interface ChatRepository {
    suspend fun sendChat(question: String): Result<String>
}
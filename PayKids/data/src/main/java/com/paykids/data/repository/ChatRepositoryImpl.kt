package com.paykids.data.repository

import com.paykids.data.datasource.ChatRemoteDatasource
import com.paykids.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDatasource
) : ChatRepository {

    override suspend fun sendChat(accessToken: String, question: String): Result<String> {
        val result = chatRemoteDataSource.sendChat(accessToken, question)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.response
                Result.success(data)
            } else {
                Result.failure(Exception("Send Chat Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

}
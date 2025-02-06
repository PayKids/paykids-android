package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.quest.QuestResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface QuestService {
    @GET("/Quest/list")
    suspend fun getQuests(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<QuestResponseDTO>>
}
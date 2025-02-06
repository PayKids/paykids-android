package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.achievement.AchieveResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AchievementService {
    @GET("/Achievement/list")
    suspend fun getAchievements(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<AchieveResponseDTO>>
}
package com.paykids.data.datasource

import com.paykids.data.model.achievement.AchieveResponseDTO

interface AchievementRemoteDatasource {
    suspend fun getAchievements(accessToken: String): Result<AchieveResponseDTO>
}
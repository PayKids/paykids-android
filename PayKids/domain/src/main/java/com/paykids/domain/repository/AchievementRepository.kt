package com.paykids.domain.repository

import com.paykids.domain.model.achievement.AchievementInfo

interface AchievementRepository {
    suspend fun getAchievements(accessToken: String): Result<List<AchievementInfo>>
}
package com.paykids.domain.usecase.acievement

import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.domain.repository.AchievementRepository

class GetAchievementUseCase(private val repository: AchievementRepository) {
    suspend operator fun invoke(accessToken: String): Result<List<AchievementInfo>> {
        return repository.getAchievements("Bearer $accessToken")
    }
}
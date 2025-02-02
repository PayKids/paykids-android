package com.paykids.data.mapper

import com.paykids.data.model.achievement.AchieveResponseDTO
import com.paykids.domain.model.achievement.AchievementInfo

fun AchieveResponseDTO.toAchievementInfo(): List<AchievementInfo> {
    return this.map { item ->
        AchievementInfo(
            name = item.name,
            isCompleted = item.isCompleted,
            image = item.imageUrl
        )
    }
}
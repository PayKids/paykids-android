package com.paykids.data.mapper

import com.paykids.data.model.achievement.AchieveResponseDTO
import com.paykids.domain.model.achievement.AchievementInfo

fun AchieveResponseDTO.toAchievementInfo(): List<AchievementInfo> {
    return this.map { item ->
        AchievementInfo(
            isCompleted = item.isCompleted,
            name = item.name,
            description = item.description,
            imageURL = item.imageURL
        )
    }
}
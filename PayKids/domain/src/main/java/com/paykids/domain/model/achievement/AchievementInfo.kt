package com.paykids.domain.model.achievement

data class AchievementInfo(
    val isCompleted: Boolean,
    val name: String,
    val description: String,
    val imageURL: String
)

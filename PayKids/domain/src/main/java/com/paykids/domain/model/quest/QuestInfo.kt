package com.paykids.domain.model.quest

data class QuestInfo(
    val name: String,
    val isCompleted: Boolean,
    val count: Int,
    val maxCount: Int
)

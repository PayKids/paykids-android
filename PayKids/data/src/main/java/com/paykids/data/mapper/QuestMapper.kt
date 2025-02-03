package com.paykids.data.mapper

import com.paykids.domain.model.quest.QuestInfo
import com.paykids.domain.model.quest.QuestItem

fun QuestResponseDTO.toQuestInfo(): List<QuestInfo> {
    return this.map { item ->
        QuestItem(
            name = item.name,
            isCompleted = item.isCompleted,
            count = item.count,
            maxCount = item.maxCount
        )
    }
}
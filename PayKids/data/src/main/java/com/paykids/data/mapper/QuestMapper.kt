package com.paykids.data.mapper

import com.paykids.data.model.quest.QuestResponseDTO
import com.paykids.domain.model.quest.QuestInfo

fun QuestResponseDTO.toQuestInfo(): List<QuestInfo> {
    return this.map { item ->
        QuestInfo(
            name = item.name,
            isCompleted = item.isComplete,
            count = item.count,
            maxCount = item.maxCount
        )
    }
}
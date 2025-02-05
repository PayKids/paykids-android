package com.paykids.domain.usecase.quest

import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.domain.model.quest.QuestInfo
import com.paykids.domain.repository.AchievementRepository
import com.paykids.domain.repository.QuestRepository

class GetQuestUseCase(private val repository: QuestRepository) {
    suspend operator fun invoke(accessToken: String): Result<List<QuestInfo>> {
        return repository.getQuests("Bearer $accessToken")
    }
}
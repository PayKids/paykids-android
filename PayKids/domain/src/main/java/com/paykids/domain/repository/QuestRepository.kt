package com.paykids.domain.repository

import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.domain.model.quest.QuestInfo
import com.paykids.domain.model.quest.QuestResponse

interface QuestRepository {
    suspend fun getQuests(accessToken: String): Result<List<QuestInfo>>
}
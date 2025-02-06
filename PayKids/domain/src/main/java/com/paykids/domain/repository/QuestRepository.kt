package com.paykids.domain.repository

import com.paykids.domain.model.quest.QuestInfo

interface QuestRepository {
    suspend fun getQuests(accessToken: String): Result<List<QuestInfo>>
}
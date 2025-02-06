package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.achievement.AchieveResponseDTO
import com.paykids.data.model.quest.QuestResponseDTO

interface QuestRemoteDatasource {
    suspend fun getQuests(accessToken: String): Result<BaseResponse<QuestResponseDTO>>
}
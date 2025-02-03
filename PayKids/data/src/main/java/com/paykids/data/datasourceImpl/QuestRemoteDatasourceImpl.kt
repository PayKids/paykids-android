package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.AchievementRemoteDatasource
import com.paykids.data.datasource.QuestRemoteDatasource
import com.paykids.data.model.achievement.AchieveResponseDTO
import com.paykids.data.model.quest.QuestResponseDTO
import com.paykids.data.service.AchievementService
import com.paykids.data.service.QuestService
import javax.inject.Inject

class QuestRemoteDatasourceImpl @Inject constructor(
    private val questService: QuestService
) : QuestRemoteDatasource {
    override suspend fun getQuests(accessToken: String): Result<QuestResponseDTO> {
        return try {
            val response = questService.getQuests(accessToken)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Quests failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Quests failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
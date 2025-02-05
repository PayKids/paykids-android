package com.paykids.data.repository

import com.paykids.data.datasource.AchievementRemoteDatasource
import com.paykids.data.datasource.QuestRemoteDatasource
import com.paykids.data.mapper.toAchievementInfo
import com.paykids.data.mapper.toQuestInfo
import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.domain.model.quest.QuestInfo
import com.paykids.domain.model.quest.QuestResponse
import com.paykids.domain.repository.AchievementRepository
import com.paykids.domain.repository.QuestRepository
import javax.inject.Inject

class QuestRepositoryImpl @Inject constructor(
    private val questRemoteDatasource: QuestRemoteDatasource
) : QuestRepository {
    override suspend fun getQuests(accessToken: String): Result<List<QuestInfo>> {
        val result = questRemoteDatasource.getQuests(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.toQuestInfo()
                Result.success(data)
            } else {
                Result.failure(Exception("get Quests Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

}
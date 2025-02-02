package com.paykids.data.repository

import com.paykids.data.datasource.AchievementRemoteDatasource
import com.paykids.data.datasource.ChatRemoteDatasource
import com.paykids.data.mapper.toAchievementInfo
import com.paykids.domain.model.achievement.AchievementInfo
import com.paykids.domain.repository.AchievementRepository
import com.paykids.domain.repository.ChatRepository
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val achievementRemoteDatasource: AchievementRemoteDatasource
) : AchievementRepository {
    override suspend fun getAchievements(accessToken: String): Result<List<AchievementInfo>> {
        val result = achievementRemoteDatasource.getAchievements(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.toAchievementInfo()
                Result.success(data)
            } else {
                Result.failure(Exception("get Achievements Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
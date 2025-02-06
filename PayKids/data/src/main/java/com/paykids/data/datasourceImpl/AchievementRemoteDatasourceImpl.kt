package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.AchievementRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.achievement.AchieveResponseDTO
import com.paykids.data.service.AchievementService
import javax.inject.Inject

class AchievementRemoteDatasourceImpl @Inject constructor(
    private val achievementService: AchievementService
) : AchievementRemoteDatasource {
    override suspend fun getAchievements(accessToken: String): Result<BaseResponse<AchieveResponseDTO>> {
        return try {
            val response = achievementService.getAchievements(accessToken)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Achievements failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Achievements failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
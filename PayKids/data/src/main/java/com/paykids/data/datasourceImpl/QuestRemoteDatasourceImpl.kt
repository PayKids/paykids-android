package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.QuestRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.quest.QuestResponseDTO
import com.paykids.data.service.QuestService
import com.paykids.util.LoggerUtils
import javax.inject.Inject

class QuestRemoteDatasourceImpl @Inject constructor(
    private val questService: QuestService
) : QuestRemoteDatasource {
    override suspend fun getQuests(accessToken: String): Result<BaseResponse<QuestResponseDTO>> {
        return try {
            val response = questService.getQuests(accessToken)
            LoggerUtils.d(response.body().toString())
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
package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.QuizRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.QuizResponseDTO
import com.paykids.data.service.QuizService
import javax.inject.Inject

class QuizRemoteDatasourceImpl @Inject constructor(
    private val quizService: QuizService
) : QuizRemoteDatasource {
    override suspend fun getQuiz(stage: Int, number: Int): Result<BaseResponse<QuizResponseDTO>> {
        return try {
            val response = quizService.getQuiz(stage, number)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Quiz failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Quiz failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStageName(stage: Int): Result<BaseResponse<String>> {
        return try {
            val response = quizService.getStageName(stage)
            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get StageName failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get StageName failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
package com.paykids.data.datasource

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.QuizResponseDTO

interface QuizRemoteDatasource {
    suspend fun getQuiz(stage: Int, number: Int): Result<BaseResponse<QuizResponseDTO>>

    suspend fun getStageName(stage: Int): Result<BaseResponse<String>>
}
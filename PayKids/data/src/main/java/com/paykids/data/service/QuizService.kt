package com.paykids.data.service

import com.paykids.data.model.BaseResponse
import com.paykids.data.model.QuizResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuizService {
    @GET("/quiz")
    suspend fun getQuiz(
        @Query("stage") stage: Int,
        @Query("number") number: Int
    ): Response<BaseResponse<QuizResponseDTO>>

    @GET("/quiz/count")
    suspend fun getStageCount(): Response<BaseResponse<Int>>

    @GET("/quiz/stage-name")
    suspend fun getStageName(
        @Query("stage") stage: Int
    ): Response<BaseResponse<String>>

    @GET("/quiz/stage-to-go")
    suspend fun getStageToGo(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<Int>>

    @GET("/quiz/check-answer")
    suspend fun checkAnswer(
        @Header("Authorization") accessToken: String,
        @Query("stage") stage: Int,
        @Query("number") number: Int,
        @Query("answer") answer: String
    ): Response<BaseResponse<Boolean>>
}
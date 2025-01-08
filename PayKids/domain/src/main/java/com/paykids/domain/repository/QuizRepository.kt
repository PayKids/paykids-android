package com.paykids.domain.repository

import com.paykids.domain.model.quiz.Quiz

interface QuizRepository {
    suspend fun getQuiz(stage: Int, number: Int) : Result<Quiz>

    suspend fun getStageCount() : Result<Int>

    suspend fun getStageName(stage: Int) : Result<String>
}
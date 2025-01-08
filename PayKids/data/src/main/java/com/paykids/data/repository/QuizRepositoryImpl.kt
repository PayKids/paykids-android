package com.paykids.data.repository

import com.paykids.data.datasource.QuizRemoteDatasource
import com.paykids.data.mapper.toQuiz
import com.paykids.domain.model.quiz.Quiz
import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val quizDatasource: QuizRemoteDatasource
) : QuizRepository {
    override suspend fun getQuiz(stage: Int, number: Int): Result<Quiz> {
        val result = quizDatasource.getQuiz(stage, number)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data.toQuiz())
            } else {
                Result.failure(Exception("get Quiz Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getStageName(stage: Int): Result<String> {
        val result = quizDatasource.getStageName(stage)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("get StageName Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
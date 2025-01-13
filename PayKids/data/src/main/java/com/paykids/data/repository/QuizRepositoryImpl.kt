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

    override suspend fun getStageCount(): Result<Int> {
        val result = quizDatasource.getStageCount()

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("get StageCount Failed: response body is null"))
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

    override suspend fun getStageGoTo(accessToken: String): Result<Int> {
        val result = quizDatasource.getStageToGo(accessToken)

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

    override suspend fun checkAnswer(
        accessToken: String,
        stage: Int,
        number: Int,
        answer: String
    ): Result<Boolean> {
        val result = quizDatasource.checkAnswer(accessToken, stage, number, answer)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("check Answer Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
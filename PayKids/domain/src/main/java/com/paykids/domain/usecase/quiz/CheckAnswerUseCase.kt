package com.paykids.domain.usecase.quiz

import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class CheckAnswerUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(accessToken: String, stage: Int, number: Int, answer: String): Result<Boolean> {
        return repository.checkAnswer(accessToken, stage, number, answer)
    }
}
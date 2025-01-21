package com.paykids.domain.usecase.quiz

import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class GetIncorrectQuizNumbersUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(accessToken: String, stage: Int): Result<List<Int>> {
        return repository.getIncorrectQuizNumbers(accessToken, stage)
    }
}
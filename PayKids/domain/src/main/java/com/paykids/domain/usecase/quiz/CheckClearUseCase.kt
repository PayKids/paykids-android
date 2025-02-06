package com.paykids.domain.usecase.quiz

import com.paykids.domain.model.quiz.QuizClear
import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class CheckClearUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(accessToken: String, stage: Int): Result<QuizClear> {
        return repository.checkClear("Bearer $accessToken", stage)
    }
}
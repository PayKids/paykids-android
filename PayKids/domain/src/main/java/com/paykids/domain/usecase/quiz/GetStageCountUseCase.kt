package com.paykids.domain.usecase.quiz

import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class GetStageCountUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(): Result<Int> {
        return repository.getStageCount()
    }
}
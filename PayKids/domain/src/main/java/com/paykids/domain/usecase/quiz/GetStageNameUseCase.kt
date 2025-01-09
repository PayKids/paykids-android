package com.paykids.domain.usecase.quiz

import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class GetStageNameUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(stage: Int): Result<String> {
        return repository.getStageName(stage = stage)
    }
}
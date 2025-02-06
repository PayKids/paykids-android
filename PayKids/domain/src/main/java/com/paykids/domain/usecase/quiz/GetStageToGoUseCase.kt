package com.paykids.domain.usecase.quiz

import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class GetStageToGoUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(accessToken: String): Result<Int> {
        return repository.getStageGoTo("Bearer $accessToken")
    }
}
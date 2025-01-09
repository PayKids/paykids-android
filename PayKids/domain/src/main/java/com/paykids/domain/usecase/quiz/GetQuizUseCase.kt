package com.paykids.domain.usecase.quiz

import com.paykids.domain.model.quiz.Quiz
import com.paykids.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuizUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(stage: Int, number: Int): Result<Quiz> {
        return repository.getQuiz(stage = stage, number = number)
    }
}
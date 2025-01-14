package com.paykids.data.mapper

import com.paykids.data.model.QuizClearResponseDTO
import com.paykids.data.model.QuizResponseDTO
import com.paykids.domain.model.quiz.Quiz
import com.paykids.domain.model.quiz.QuizClear

fun QuizResponseDTO.toQuiz(): Quiz {
    return Quiz(
        answer = this.answer,
        choices = this.choices,
        count = this.count,
        id = this.id,
        imageURL = this.imageURL,
        number = this.number,
        question = this.question,
        quizType = this.quizType,
        stage = this.stage
    )
}

fun QuizClearResponseDTO.toQuizClear(): QuizClear {
    return QuizClear(
        message = this.message,
        isCleared = this.isCleared
    )
}

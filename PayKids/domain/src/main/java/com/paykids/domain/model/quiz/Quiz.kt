package com.paykids.domain.model.quiz

data class Quiz (
    val answer: String,
    val choices: Map<String, String>,
    val count: Int,
    val id: Int,
    val imageURL: Map<String, String>,
    val number: Int,
    val question: String,
    val quizType: String,
    val stage: Int
)
package com.paykids.data.model


import com.google.gson.annotations.SerializedName

data class QuizResponseDTO(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("choices")
    val choices: Map<String, String>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageURL")
    val imageURL: Map<String, String>,
    @SerializedName("number")
    val number: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("quizType")
    val quizType: String,
    @SerializedName("stage")
    val stage: Int
)
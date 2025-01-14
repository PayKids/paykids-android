package com.paykids.data.model


import com.google.gson.annotations.SerializedName

data class QuizClearResponseDTO(
    @SerializedName("isCleared")
    val isCleared: Boolean,
    @SerializedName("message")
    val message: String
)
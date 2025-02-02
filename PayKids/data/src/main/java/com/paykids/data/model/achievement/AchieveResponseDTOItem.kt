package com.paykids.data.model.achievement


import com.google.gson.annotations.SerializedName

data class AchieveResponseDTOItem(
    @SerializedName("isCompleted")
    val isCompleted: Boolean,
    @SerializedName("name")
    val name: String
)
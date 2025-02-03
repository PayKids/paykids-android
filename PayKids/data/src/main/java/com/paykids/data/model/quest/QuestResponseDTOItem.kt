package com.paykids.data.model.quest


import com.google.gson.annotations.SerializedName

data class QuestResponseDTOItem(
    @SerializedName("count")
    val count: Int,
    @SerializedName("isComplete")
    val isComplete: Boolean,
    @SerializedName("maxCount")
    val maxCount: Int,
    @SerializedName("name")
    val name: String
)
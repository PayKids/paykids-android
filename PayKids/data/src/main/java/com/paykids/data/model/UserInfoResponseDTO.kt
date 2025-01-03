package com.paykids.data.model


import com.google.gson.annotations.SerializedName

data class UserInfoResponseDTO(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImageURL")
    val profileImageURL: String,
    @SerializedName("stageStatus")
    val stageStatus: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("uuid")
    val uuid: String
)
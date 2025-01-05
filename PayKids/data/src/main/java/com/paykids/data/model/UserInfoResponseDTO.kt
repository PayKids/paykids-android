package com.paykids.data.model


import com.google.gson.annotations.SerializedName

data class UserInfoResponseDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("sub")
    val sub: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profileImageURL")
    val profileImageURL: String,
    @SerializedName("stageStatus")
    val stageStatus: Int
)
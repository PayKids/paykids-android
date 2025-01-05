package com.paykids.data.model


import com.google.gson.annotations.SerializedName

data class UserTokenResponseDTO(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("tokenType")
    val tokenType: String,
    @SerializedName("isRegistered")
    val isRegistered: Boolean
)
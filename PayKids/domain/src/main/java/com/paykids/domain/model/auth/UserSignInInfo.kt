package com.paykids.domain.model.auth

data class UserSignInInfo(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val isRegistered: Boolean
)
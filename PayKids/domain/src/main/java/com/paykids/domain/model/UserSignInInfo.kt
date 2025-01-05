package com.paykids.domain.model

import com.paykids.domain.enums.AuthProvider

data class UserSignInInfo(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val isRegistered: Boolean
)
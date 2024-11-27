package com.paykids.domain.model

import com.paykids.domain.enums.AuthProvider

data class SignInInfo(
    val idToken: String,
    val provider: AuthProvider,
    val email: String? = null,
    val name: String? = null,
    val isRegister: Boolean? = false,
)
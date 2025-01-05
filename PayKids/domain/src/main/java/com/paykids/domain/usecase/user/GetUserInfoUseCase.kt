package com.paykids.domain.usecase.user

import com.paykids.domain.model.auth.UserSignInInfo
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.AuthRepository
import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String): Result<UserInfo> {
        return repository.getUserInfo("Bearer $accessToken")
    }
}
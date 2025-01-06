package com.paykids.domain.usecase.user

import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class ChangeNicknameUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String, newNickname: String): Result<String> {
        return repository.changeNickname("Bearer $accessToken", newNickname)
    }
}
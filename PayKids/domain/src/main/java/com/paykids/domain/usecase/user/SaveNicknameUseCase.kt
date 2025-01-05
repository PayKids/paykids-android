package com.paykids.domain.usecase.user

import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class SaveNicknameUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String, nickname: String): Result<String> {
        return repository.saveNickname("Bearer $accessToken", nickname)
    }
}
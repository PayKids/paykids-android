package com.paykids.domain.usecase.user

import com.paykids.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(accessToken: String): Result<String> {
        return userRepository.deleteUser(accessToken)
    }
}


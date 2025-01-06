package com.paykids.domain.usecase.user

import com.paykids.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(accessToken: String, imageFile: File, mimeType: String): Result<String> {
        return userRepository.updateProfileImage(accessToken, imageFile, mimeType)
    }
}


package com.paykids.domain.usecase.user

import com.paykids.domain.repository.UserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(accessToken: String, file: MultipartBody.Part) =
        userRepository.updateProfileImage(accessToken, file)
}
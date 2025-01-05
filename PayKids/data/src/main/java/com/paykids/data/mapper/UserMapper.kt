package com.paykids.data.mapper

import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.model.UserTokenResponseDTO
import com.paykids.domain.model.auth.UserSignInInfo
import com.paykids.domain.model.user.UserInfo

object UserMapper {

    fun mapperToResponseEntity(item: UserInfoResponseDTO): UserInfo {
        return item.run {
            UserInfo(
                nickname = item.nickname,
                email = item.email,
                profileImageURL = item.profileImageURL
            )
        }
    }
}
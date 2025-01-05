package com.paykids.data.mapper

import com.paykids.data.model.UserTokenResponseDTO
import com.paykids.domain.model.auth.UserSignInInfo

object SignMapper {

    fun mapperToResponseEntity(item: UserTokenResponseDTO): UserSignInInfo {
        return item.run {
            UserSignInInfo(
                accessToken = item.accessToken,
                refreshToken = item.refreshToken,
                tokenType = item.tokenType,
                isRegistered = item.isRegistered
            )
        }
    }
}
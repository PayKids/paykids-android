package com.paykids.domain.model

data class ChatItem(
    val chatId: Int,
    val content: String,
    val isMine: Boolean,
    val nickname: String
)
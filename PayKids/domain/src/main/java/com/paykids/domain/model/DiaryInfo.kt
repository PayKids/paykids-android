package com.paykids.domain.model

data class DiaryInfo(
    val content: String,
    val diaryEntryDate: String,
    val diaryId: Int,
    val emotion: String?,
    val isBookmarked: Boolean,
    val nickname: String
)
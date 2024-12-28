package com.paykids.presentation.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Constants {
    fun formatDateToKorean(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateString)
            val calendar = Calendar.getInstance().apply { time = date }

            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            "${month}월 ${day}일"
        } catch (e: Exception) {
            dateString // 파싱 실패 시 원본 반환
        }
    }

    fun formatAmount(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount)
    }
}
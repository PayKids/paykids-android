package com.paykids.domain.model.allowance

data class DayInfo(
    val id: Int,
    val date: String,
    val allowanceType: String,
    val category: String,
    val amount: Int,
    val memo: String
)

package com.paykids.data.model.expense


import com.google.gson.annotations.SerializedName

data class MonthDailyDTOItem(
    @SerializedName("allowanceType")
    val allowanceType: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("date")
    val date: String
)
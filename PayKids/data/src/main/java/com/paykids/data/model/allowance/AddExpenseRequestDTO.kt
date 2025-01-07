package com.paykids.data.model.allowance


import com.google.gson.annotations.SerializedName

data class AddExpenseRequestDTO(
    @SerializedName("allowanceType")
    val allowanceType: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("memo")
    val memo: String
)
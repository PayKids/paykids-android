package com.paykids.data.model.expense


import com.google.gson.annotations.SerializedName

data class ExpenseRequestDTO(
    @SerializedName("allowanceType")
    val allowanceType: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("memo")
    val memo: String
)
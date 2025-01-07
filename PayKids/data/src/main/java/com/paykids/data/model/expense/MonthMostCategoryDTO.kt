package com.paykids.data.model.expense


import com.google.gson.annotations.SerializedName

data class MonthMostCategoryDTO(
    @SerializedName("allowanceType")
    val allowanceType: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("percent")
    val percent: String
)
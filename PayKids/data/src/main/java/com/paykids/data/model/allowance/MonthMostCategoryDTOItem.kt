package com.paykids.data.model.allowance


import com.google.gson.annotations.SerializedName

data class MonthMostCategoryDTOItem(
    @SerializedName("allowanceType")
    val allowanceType: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("percent")
    val percent: String
)
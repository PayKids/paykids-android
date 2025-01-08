package com.paykids.data.model.allowanceCategory


import com.google.gson.annotations.SerializedName

data class CategoryListDTOItem(
    @SerializedName("allowanceType")
    val allowanceType: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)
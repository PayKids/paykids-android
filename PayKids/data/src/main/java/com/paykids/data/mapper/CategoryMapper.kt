package com.paykids.data.mapper

import com.paykids.data.model.allowanceCategory.CategoryListDTO
import com.paykids.domain.model.allowanceCategory.CategoryInfo

fun CategoryListDTO.toCategoryInfo(): List<CategoryInfo> {
    return this.map { item ->
        CategoryInfo(
            category = item.title
        )
    }
}

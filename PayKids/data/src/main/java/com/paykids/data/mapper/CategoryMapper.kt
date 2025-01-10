package com.paykids.data.mapper

import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowanceCategory.CategoryListDTO
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.model.allowanceCategory.CategoryInfo

fun CategoryListDTO.toCategoryInfo(): List<CategoryInfo> {
    return this.map { item ->
        CategoryInfo(
            category = item.title
        )
    }
}

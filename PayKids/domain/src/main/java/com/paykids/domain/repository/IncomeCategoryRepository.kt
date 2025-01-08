package com.paykids.domain.repository

import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.model.allowance.MonthMostCategory
import com.paykids.domain.model.allowanceCategory.CategoryInfo

interface IncomeCategoryRepository {

    suspend fun getIncomeCategoryList(
        accessToken: String,
    ): Result<List<CategoryInfo>>

    suspend fun saveIncomeCategory(
        accessToken: String,
        category: String
    ): Result<Boolean>

    suspend fun deleteIncomeCategory(
        accessToken: String,
        category: String
    ): Result<Boolean>

}
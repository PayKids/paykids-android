package com.paykids.domain.repository

import com.paykids.domain.model.expense.DailyExpenseInfo
import com.paykids.domain.model.expense.MonthAllCategory
import com.paykids.domain.model.expense.MonthCategoryExpense
import com.paykids.domain.model.expense.MonthMostCategory

interface ExpenseRepository {
    suspend fun getMonthTotalExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int>

    suspend fun getMonthMostExpenseCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<MonthMostCategory>

    suspend fun getMonthDailyExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<DailyExpenseInfo>>

    suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<List<MonthCategoryExpense>>

    suspend fun getMonthAllCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategory>>

    suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<String>
}
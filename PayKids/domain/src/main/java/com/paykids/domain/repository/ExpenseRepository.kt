package com.paykids.domain.repository

import com.paykids.domain.model.expense.DailyExpenseInfo
import com.paykids.domain.model.user.UserInfo
import java.io.File

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
    ): Result<String>

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
    ): Result<String>

    suspend fun getMonthAllCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<String>

    suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<String>
}
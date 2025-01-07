package com.paykids.domain.repository

import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.model.allowance.MonthMostCategory

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
    ): Result<List<MonthDailyInfo>>

    suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<List<MonthCategoryInfo>>

    suspend fun getMonthAllCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategoryInfo>>

    suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<List<DayInfo>>

    suspend fun saveExpense(
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean>

    suspend fun updateExpense(
        id: Int,
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean>

    suspend fun deleteExpense(
        id: Int, accessToken: String
    ): Result<Boolean>
}
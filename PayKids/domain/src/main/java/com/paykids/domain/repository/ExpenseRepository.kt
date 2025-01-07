package com.paykids.domain.repository

import com.paykids.domain.model.expense.DayExpense
import com.paykids.domain.model.expense.MonthAllCategory
import com.paykids.domain.model.expense.MonthCategoryExpense
import com.paykids.domain.model.expense.MonthDailyExpenseInfo
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
    ): Result<List<MonthDailyExpenseInfo>>

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
    ): Result<List<DayExpense>>

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
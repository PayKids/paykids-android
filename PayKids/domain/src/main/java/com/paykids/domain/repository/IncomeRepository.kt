package com.paykids.domain.repository

import com.paykids.domain.model.expenseIncome.DayInfo
import com.paykids.domain.model.expenseIncome.MonthAllCategoryInfo
import com.paykids.domain.model.expenseIncome.MonthCategoryInfo
import com.paykids.domain.model.expenseIncome.MonthDailyInfo
import com.paykids.domain.model.expenseIncome.MonthMostCategory

interface IncomeRepository {
    suspend fun getMonthTotalIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int>

    suspend fun getMonthDailyIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthDailyInfo>>

    suspend fun getMonthCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<List<MonthCategoryInfo>>

    suspend fun getMonthAllCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategoryInfo>>

    suspend fun getDayIncome(
        accessToken: String,
        localDate: String
    ): Result<List<DayInfo>>

    suspend fun saveIncome(
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean>

    suspend fun updateIncome(
        id: Int,
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean>

    suspend fun deleteIncome(
        id: Int, accessToken: String
    ): Result<Boolean>
}
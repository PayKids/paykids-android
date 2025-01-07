package com.paykids.data.repository

import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.datasource.UserRemoteDatasource
import com.paykids.data.mapper.UserMapper
import com.paykids.data.mapper.toDailyExpenseInfoList
import com.paykids.data.mapper.toMonthCategoryExpense
import com.paykids.domain.model.expense.DailyExpenseInfo
import com.paykids.domain.model.expense.MonthCategoryExpense
import com.paykids.domain.model.expense.MonthMostCategory
import com.paykids.domain.model.user.UserInfo
import com.paykids.domain.repository.ExpenseRepository
import com.paykids.domain.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseRemoteDatasource: ExpenseRemoteDatasource
) : ExpenseRepository {
    override suspend fun getMonthTotalExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int> {
        val result = expenseRemoteDatasource.getMonthTotalExpense(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("get Month Total Expense Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthMostExpenseCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<MonthMostCategory> {
        val result = expenseRemoteDatasource.getMonthMostExpenseCategory(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val mostCategoryInfo = MonthMostCategory(
                    category = data.category,
                    amount = data.amount
                )
                Result.success(mostCategoryInfo)
            } else {
                Result.failure(Exception("get Month Most Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthDailyExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<DailyExpenseInfo>> {
        val result = expenseRemoteDatasource.getMonthDailyExpense(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val dailyExpenseInfoList = data.toDailyExpenseInfoList()
                Result.success(dailyExpenseInfoList)
            } else {
                Result.failure(Exception("get Month Daily Expense Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<List<MonthCategoryExpense>> {
        val result =
            expenseRemoteDatasource.getMonthCategoryExpense(accessToken, year, month, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val categoryExpenseInfo = data.toMonthCategoryExpense()
                Result.success(categoryExpenseInfo)
            } else {
                Result.failure(Exception("get Month Total Expense Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthAllCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getDayExpense(accessToken: String, localDate: String): Result<String> {
        TODO("Not yet implemented")
    }

}
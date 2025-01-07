package com.paykids.data.repository

import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.mapper.toDailyInfoList
import com.paykids.data.mapper.toDayInfoList
import com.paykids.data.mapper.toMonthAllCategoryList
import com.paykids.data.mapper.toMonthCategoryList
import com.paykids.data.model.expense.AddExpenseRequestDTO
import com.paykids.data.model.expense.UpdateExpenseRequestDTO
import com.paykids.domain.model.expenseIncome.DayInfo
import com.paykids.domain.model.expenseIncome.MonthAllCategoryInfo
import com.paykids.domain.model.expenseIncome.MonthCategoryInfo
import com.paykids.domain.model.expenseIncome.MonthDailyInfo
import com.paykids.domain.model.expenseIncome.MonthMostCategory
import com.paykids.domain.repository.ExpenseRepository
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
    ): Result<List<MonthDailyInfo>> {
        val result = expenseRemoteDatasource.getMonthDailyExpense(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val dailyExpenseInfoList = data.toDailyInfoList()
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
    ): Result<List<MonthCategoryInfo>> {
        val result =
            expenseRemoteDatasource.getMonthCategoryExpense(accessToken, year, month, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val categoryExpenseInfo = data.toMonthCategoryList()
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
    ): Result<List<MonthAllCategoryInfo>> {
        val result =
            expenseRemoteDatasource.getMonthAllCategory(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val allCategoryInfo = data.toMonthAllCategoryList()
                Result.success(allCategoryInfo)
            } else {
                Result.failure(Exception("get Month All Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<List<DayInfo>> {
        val result =
            expenseRemoteDatasource.getDayExpense(accessToken, localDate)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val dayExpenseInfo = data.toDayInfoList()
                Result.success(dayExpenseInfo)
            } else {
                Result.failure(Exception("get Day Expense Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun saveExpense(
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        val result =
            expenseRemoteDatasource.saveExpense(
                accessToken, AddExpenseRequestDTO(allowanceType, amount, category, date, memo)
            )

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("save Expense Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun updateExpense(
        id: Int,
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        val result =
            expenseRemoteDatasource.updateExpense(
                accessToken,
                UpdateExpenseRequestDTO(allowanceType, amount, category, date, id, memo)
            )

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("update Expense Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }


    override suspend fun deleteExpense(id: Int, accessToken: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

}
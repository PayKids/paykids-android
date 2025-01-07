package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.expense.DayExpenseResponseDTO
import com.paykids.data.model.UserInfoResponseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.data.model.expense.MonthMostCategoryDTO
import com.paykids.data.service.ExpenseService
import javax.inject.Inject

class ExpenseRemoteDatasourceImpl @Inject constructor(
    private val expenseService: ExpenseService
) : ExpenseRemoteDatasource {
    override suspend fun getMonthTotalExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<Int>> {
        return try {
            val response = expenseService.getMonthTotalExpense(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Total Expense failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Total Expense failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthMostExpenseCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthMostCategoryDTO>> {
        return try {
            val response = expenseService.getMonthMostExpenseCategory(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Most Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Most Category failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthDailyExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthDailyExpenseDTO>> {
        return try {
            val response = expenseService.getMonthDailyExpense(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Daily Expense failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Daily Expense failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<BaseResponse<UserInfoResponseDTO>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMonthAllCategory(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<UserInfoResponseDTO>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayExpenseResponseDTO>> {
        TODO("Not yet implemented")
    }
}
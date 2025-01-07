package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowance.MonthMostCategoryDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
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
    ): Result<BaseResponse<MonthDailyDTO>> {
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
    ): Result<BaseResponse<MonthCategoryDTO>> {
        return try {
            val response =
                expenseService.getMonthCategoryExpense(accessToken, year, month, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Category Expense failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Category Expense failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthAllCategoryExpense(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthAllCategoryDTO>> {
        return try {
            val response = expenseService.getMonthAllCategoryExpense(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month All Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month All Category failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDayExpense(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayDTO>> {
        return try {
            val response = expenseService.getDayExpense(accessToken, localDate)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Day Expense failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Day Expense failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveExpense(
        accessToken: String,
        expenseInfo: AddExpenseRequestDTO
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = expenseService.saveExpense(accessToken, expenseInfo)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("save Expense failed: response body is null"))
                }
            } else {
                Result.failure(Exception("save Expense failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateExpense(
        accessToken: String,
        newExpenseInfo: UpdateExpenseRequestDTO
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = expenseService.updateExpense(accessToken, newExpenseInfo)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("update Expense failed: response body is null"))
                }
            } else {
                Result.failure(Exception("update Expense failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteExpense(
        id: Int,
        accessToken: String
    ): Result<BaseResponse<Boolean>> {
        TODO("Not yet implemented")
    }
}
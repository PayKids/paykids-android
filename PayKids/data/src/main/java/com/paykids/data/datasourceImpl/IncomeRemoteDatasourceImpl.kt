package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.IncomeRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
import com.paykids.data.service.IncomeService
import javax.inject.Inject

class IncomeRemoteDatasourceImpl @Inject constructor(
    private val incomeService: IncomeService
) : IncomeRemoteDatasource {
    override suspend fun getMonthTotalIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<Int>> {
        return try {
            val response = incomeService.getMonthTotalIncome(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Total Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Total Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthDailyIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthDailyDTO>> {
        return try {
            val response = incomeService.getMonthDailyIncome(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Daily Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Daily Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<BaseResponse<MonthCategoryDTO>> {
        return try {
            val response = incomeService.getMonthCategoryIncome(accessToken, year, month, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month Category Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month Category Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMonthAllCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<MonthAllCategoryDTO>> {
        return try {
            val response = incomeService.getMonthAllCategoryIncome(accessToken, year, month)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Month All Category Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Month All Category Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDayIncome(
        accessToken: String,
        localDate: String
    ): Result<BaseResponse<DayDTO>> {
        return try {
            val response = incomeService.getDayIncome(accessToken, localDate)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Day Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Day Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIncome(
        accessToken: String,
        expenseInfo: AddExpenseRequestDTO
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = incomeService.saveIncome(accessToken, expenseInfo)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("save Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("save Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateIncome(
        accessToken: String,
        newExpenseInfo: UpdateExpenseRequestDTO
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = incomeService.updateIncome(accessToken, newExpenseInfo)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("update Income failed: response body is null"))
                }
            } else {
                Result.failure(Exception("update Income failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteIncome(id: Int, accessToken: String): Result<BaseResponse<Boolean>> {
        TODO("Not yet implemented")
    }

}
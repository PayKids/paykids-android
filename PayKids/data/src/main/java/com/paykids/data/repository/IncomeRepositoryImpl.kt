package com.paykids.data.repository

import com.paykids.data.datasource.IncomeRemoteDatasource
import com.paykids.data.mapper.toDailyInfoList
import com.paykids.data.mapper.toDayInfoList
import com.paykids.data.mapper.toMonthAllCategoryList
import com.paykids.data.mapper.toMonthCategoryList
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class IncomeRepositoryImpl @Inject constructor(
    private val incomeRemoteDatasource: IncomeRemoteDatasource
) : IncomeRepository {
    override suspend fun getMonthTotalIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<Int> {
        val result = incomeRemoteDatasource.getMonthTotalIncome(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                Result.success(res.data)
            } else {
                Result.failure(Exception("get Month Total Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthDailyIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthDailyInfo>> {
        val result = incomeRemoteDatasource.getMonthDailyIncome(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val dailyExpenseInfoList = data.toDailyInfoList()
                Result.success(dailyExpenseInfoList)
            } else {
                Result.failure(Exception("get Month Daily Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int,
        category: String
    ): Result<List<MonthCategoryInfo>> {
        val result =
            incomeRemoteDatasource.getMonthCategoryIncome(accessToken, year, month, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val categoryExpenseInfo = data.toMonthCategoryList()
                Result.success(categoryExpenseInfo)
            } else {
                Result.failure(Exception("get Month Category Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getMonthAllCategoryIncome(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthAllCategoryInfo>> {
        val result =
            incomeRemoteDatasource.getMonthAllCategoryIncome(accessToken, year, month)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val allCategoryInfo = data.toMonthAllCategoryList()
                Result.success(allCategoryInfo)
            } else {
                Result.failure(Exception("get Month All Category Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun getDayIncome(
        accessToken: String,
        localDate: String
    ): Result<List<DayInfo>> {
        val result =
            incomeRemoteDatasource.getDayIncome(accessToken, localDate)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                val dayExpenseInfo = data.toDayInfoList()
                Result.success(dayExpenseInfo)
            } else {
                Result.failure(Exception("get Day Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun saveIncome(
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        val result =
            incomeRemoteDatasource.saveIncome(
                accessToken, AddExpenseRequestDTO(allowanceType, amount, category, date, memo)
            )

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("save Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun updateIncome(
        id: Int,
        accessToken: String,
        date: String,
        allowanceType: String,
        category: String,
        amount: Int,
        memo: String
    ): Result<Boolean> {
        val result =
            incomeRemoteDatasource.updateIncome(
                accessToken,
                UpdateExpenseRequestDTO(allowanceType, amount, category, date, id, memo)
            )

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("update Income Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun deleteIncome(id: Int, accessToken: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}
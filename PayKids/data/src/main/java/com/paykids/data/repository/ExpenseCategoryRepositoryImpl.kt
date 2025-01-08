package com.paykids.data.repository

import com.paykids.data.datasource.ExpenseCategoryRemoteDatasource
import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.datasource.IncomeCategoryRemoteDatasource
import com.paykids.data.mapper.toDailyInfoList
import com.paykids.data.mapper.toDayInfoList
import com.paykids.data.mapper.toMonthAllCategoryList
import com.paykids.data.mapper.toMonthCategoryList
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
import com.paykids.data.model.allowanceCategory.CategoryListDTO
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo
import com.paykids.domain.model.allowance.MonthMostCategory
import com.paykids.domain.model.allowanceCategory.CategoryInfo
import com.paykids.domain.repository.ExpenseCategoryRepository
import com.paykids.domain.repository.ExpenseRepository
import javax.inject.Inject

class ExpenseCategoryRepositoryImpl @Inject constructor(
    private val expenseCategoryRemoteDatasource: ExpenseCategoryRemoteDatasource
) : ExpenseCategoryRepository {

    override suspend fun getExpenseCategoryList(accessToken: String): Result<List<CategoryInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveExpenseCategory(
        accessToken: String,
        category: String
    ): Result<Boolean> {
        val result =
            expenseCategoryRemoteDatasource.saveExpenseCategory(accessToken, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("save Expense Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun deleteExpenseCategory(
        accessToken: String,
        category: String
    ): Result<Boolean> {
        val result =
            expenseCategoryRemoteDatasource.deleteExpenseCategory(accessToken, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("delete Expense Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

}
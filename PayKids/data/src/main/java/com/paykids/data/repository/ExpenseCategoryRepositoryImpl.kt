package com.paykids.data.repository

import com.paykids.data.datasource.ExpenseCategoryRemoteDatasource
import com.paykids.data.mapper.toCategoryInfo
import com.paykids.domain.model.allowanceCategory.CategoryInfo
import com.paykids.domain.repository.ExpenseCategoryRepository
import javax.inject.Inject

class ExpenseCategoryRepositoryImpl @Inject constructor(
    private val expenseCategoryRemoteDatasource: ExpenseCategoryRemoteDatasource
) : ExpenseCategoryRepository {

    override suspend fun getExpenseCategoryList(accessToken: String): Result<List<CategoryInfo>> {
        val result =
            expenseCategoryRemoteDatasource.getExpenseCategoryList(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data.toCategoryInfo()
                Result.success(data)
            } else {
                Result.failure(Exception("get Expense Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun addExpenseCategory(
        accessToken: String,
        category: String
    ): Result<Boolean> {
        val result =
            expenseCategoryRemoteDatasource.addExpenseCategory(accessToken, category)

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
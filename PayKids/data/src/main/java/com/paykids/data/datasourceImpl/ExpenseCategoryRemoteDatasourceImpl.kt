package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.ExpenseCategoryRemoteDatasource
import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowance.AddExpenseRequestDTO
import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.data.model.allowance.MonthMostCategoryDTO
import com.paykids.data.model.allowance.UpdateExpenseRequestDTO
import com.paykids.data.model.allowanceCategory.CategoryListDTO
import com.paykids.data.service.ExpenseCategoryService
import com.paykids.data.service.ExpenseService
import javax.inject.Inject

class ExpenseCategoryRemoteDatasourceImpl @Inject constructor(
    private val expenseCategoryService: ExpenseCategoryService
) : ExpenseCategoryRemoteDatasource {

    override suspend fun getExpenseCategoryList(accessToken: String): Result<BaseResponse<CategoryListDTO>> {
        return try {
            val response = expenseCategoryService.getExpenseCategoryList(accessToken)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Expense Category List failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Expense Category List failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveExpenseCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = expenseCategoryService.saveExpenseCategory(accessToken, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("save Expense Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("save Expense Category failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteExpenseCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = expenseCategoryService.deleteExpenseCategory(accessToken, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("delete Expense Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("delete Expense Category failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
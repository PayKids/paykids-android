package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.ExpenseCategoryRemoteDatasource
import com.paykids.data.datasource.ExpenseRemoteDatasource
import com.paykids.data.datasource.IncomeCategoryRemoteDatasource
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
import com.paykids.data.service.IncomeCategoryService
import javax.inject.Inject

class IncomeCategoryRemoteDatasourceImpl @Inject constructor(
    private val incomeCategoryService: IncomeCategoryService
) : IncomeCategoryRemoteDatasource {

    override suspend fun getIncomeCategoryList(accessToken: String): Result<BaseResponse<CategoryListDTO>> {
        return try {
            val response = incomeCategoryService.getIncomeCategoryList(accessToken)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("get Income Category List failed: response body is null"))
                }
            } else {
                Result.failure(Exception("get Income Category List failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIncomeCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = incomeCategoryService.saveIncomeCategory(accessToken, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("save Income Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("save Income Category failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteIncomeCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = incomeCategoryService.deleteIncomeCategory(accessToken, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("delete Income Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("delete Income Category failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
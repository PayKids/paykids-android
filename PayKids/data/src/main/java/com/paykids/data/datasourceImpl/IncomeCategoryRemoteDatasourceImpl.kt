package com.paykids.data.datasourceImpl

import com.paykids.data.datasource.IncomeCategoryRemoteDatasource
import com.paykids.data.model.BaseResponse
import com.paykids.data.model.allowanceCategory.CategoryListDTO
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

    override suspend fun addIncomeCategory(
        accessToken: String,
        category: String
    ): Result<BaseResponse<Boolean>> {
        return try {
            val response = incomeCategoryService.addIncomeCategory(accessToken, category)

            if (response.isSuccessful) {
                val res = response.body()
                if (res != null) {
                    Result.success(res)
                } else {
                    Result.failure(Exception("add Income Category failed: response body is null"))
                }
            } else {
                Result.failure(Exception("add Income Category failed: ${response.message()}"))
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
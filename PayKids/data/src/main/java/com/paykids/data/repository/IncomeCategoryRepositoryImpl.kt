package com.paykids.data.repository

import com.paykids.data.datasource.IncomeCategoryRemoteDatasource
import com.paykids.domain.model.allowanceCategory.CategoryInfo
import com.paykids.domain.repository.IncomeCategoryRepository
import javax.inject.Inject

class IncomeCategoryRepositoryImpl @Inject constructor(
    private val incomeCategoryRemoteDatasource: IncomeCategoryRemoteDatasource
) : IncomeCategoryRepository {

    override suspend fun getIncomeCategoryList(accessToken: String): Result<List<CategoryInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveIncomeCategory(
        accessToken: String,
        category: String
    ): Result<Boolean> {
        val result =
            incomeCategoryRemoteDatasource.saveIncomeCategory(accessToken, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("save Income Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun deleteIncomeCategory(
        accessToken: String,
        category: String
    ): Result<Boolean> {
        val result =
            incomeCategoryRemoteDatasource.deleteIncomeCategory(accessToken, category)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                Result.success(data)
            } else {
                Result.failure(Exception("delete Income Category Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

}
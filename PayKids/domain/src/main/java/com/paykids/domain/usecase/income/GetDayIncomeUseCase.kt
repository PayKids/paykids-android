package com.paykids.domain.usecase.income

import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.repository.IncomeRepository
import javax.inject.Inject

class GetDayIncomeUseCase @Inject constructor(private val repository: IncomeRepository) {
    suspend operator fun invoke(
        accessToken: String,
        localDate: String
    ): Result<List<DayInfo>> {
        return repository.getDayIncome("Bearer $accessToken", localDate)
    }
}
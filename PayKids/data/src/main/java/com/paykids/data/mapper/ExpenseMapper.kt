package com.paykids.data.mapper

import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.domain.model.expense.DailyExpenseInfo

fun MonthDailyExpenseDTO.toDailyExpenseInfoList(): List<DailyExpenseInfo> {
    return this.map { item ->
        DailyExpenseInfo(
            date = item.date,
            amount = item.amount
        )
    }
}

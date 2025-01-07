package com.paykids.data.mapper

import com.paykids.data.model.expense.MonthCategoryExpenseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.domain.model.expense.DailyExpenseInfo
import com.paykids.domain.model.expense.MonthCategoryExpense

fun MonthDailyExpenseDTO.toDailyExpenseInfoList(): List<DailyExpenseInfo> {
    return this.map { item ->
        DailyExpenseInfo(
            date = item.date,
            amount = item.amount
        )
    }
}

fun MonthCategoryExpenseDTO.toMonthCategoryExpense(): List<MonthCategoryExpense> {
    return this.map { item ->
        MonthCategoryExpense(
            date = item.date,
            amount = item.amount,
            memo = item.memo
        )
    }
}
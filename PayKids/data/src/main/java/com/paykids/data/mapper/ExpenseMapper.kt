package com.paykids.data.mapper

import com.paykids.data.model.expense.DayDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryDTO
import com.paykids.data.model.expense.MonthDailyDTO
import com.paykids.domain.model.expense.DayExpense
import com.paykids.domain.model.expense.MonthAllCategory
import com.paykids.domain.model.expense.MonthCategoryExpense
import com.paykids.domain.model.expense.MonthDailyExpenseInfo

fun MonthDailyDTO.toDailyExpenseInfoList(): List<MonthDailyExpenseInfo> {
    return this.map { item ->
        MonthDailyExpenseInfo(
            date = item.date,
            amount = item.amount
        )
    }
}

fun MonthCategoryDTO.toMonthCategoryExpense(): List<MonthCategoryExpense> {
    return this.map { item ->
        MonthCategoryExpense(
            date = item.date,
            amount = item.amount,
            memo = item.memo
        )
    }
}

fun MonthAllCategoryDTO.toMonthAllCategory(): List<MonthAllCategory> {
    return this.map { item ->
        MonthAllCategory(
            category = item.category,
            amount = item.amount,
            percent = item.percent
        )
    }
}

fun DayDTO.toDayExpense(): List<DayExpense> {
    return this.map { item ->
        DayExpense(
            category = item.category,
            amount = item.amount,
            memo = item.memo
        )
    }
}
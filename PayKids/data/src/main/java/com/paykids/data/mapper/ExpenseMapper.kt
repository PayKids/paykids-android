package com.paykids.data.mapper

import com.paykids.data.model.expense.DayExpenseDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryExpenseDTO
import com.paykids.data.model.expense.MonthDailyExpenseDTO
import com.paykids.domain.model.expense.DayExpense
import com.paykids.domain.model.expense.MonthDailyExpenseInfo
import com.paykids.domain.model.expense.MonthAllCategory
import com.paykids.domain.model.expense.MonthCategoryExpense

fun MonthDailyExpenseDTO.toDailyExpenseInfoList(): List<MonthDailyExpenseInfo> {
    return this.map { item ->
        MonthDailyExpenseInfo(
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

fun MonthAllCategoryDTO.toMonthAllCategory(): List<MonthAllCategory> {
    return this.map { item ->
        MonthAllCategory(
            category = item.category,
            amount = item.amount,
            percent = item.percent
        )
    }
}

fun DayExpenseDTO.toDayExpense(): List<DayExpense> {
    return this.map { item ->
        DayExpense(
            category = item.category,
            amount = item.amount,
            memo = item.memo
        )
    }
}
package com.paykids.data.mapper

import com.paykids.data.model.expense.DayDTO
import com.paykids.data.model.expense.MonthAllCategoryDTO
import com.paykids.data.model.expense.MonthCategoryDTO
import com.paykids.data.model.expense.MonthDailyDTO
import com.paykids.domain.model.expenseIncome.DayInfo
import com.paykids.domain.model.expenseIncome.MonthAllCategoryInfo
import com.paykids.domain.model.expenseIncome.MonthCategoryInfo
import com.paykids.domain.model.expenseIncome.MonthDailyInfo

fun MonthDailyDTO.toDailyInfoList(): List<MonthDailyInfo> {
    return this.map { item ->
        MonthDailyInfo(
            date = item.date,
            amount = item.amount
        )
    }
}

fun MonthCategoryDTO.toMonthCategoryList(): List<MonthCategoryInfo> {
    return this.map { item ->
        MonthCategoryInfo(
            date = item.date,
            amount = item.amount,
            memo = item.memo
        )
    }
}

fun MonthAllCategoryDTO.toMonthAllCategoryList(): List<MonthAllCategoryInfo> {
    return this.map { item ->
        MonthAllCategoryInfo(
            category = item.category,
            amount = item.amount,
            percent = item.percent
        )
    }
}

fun DayDTO.toDayInfoList(): List<DayInfo> {
    return this.map { item ->
        DayInfo(
            category = item.category,
            amount = item.amount,
            memo = item.memo
        )
    }
}
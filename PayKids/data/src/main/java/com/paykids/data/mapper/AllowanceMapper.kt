package com.paykids.data.mapper

import com.paykids.data.model.allowance.DayDTO
import com.paykids.data.model.allowance.MonthAllCategoryDTO
import com.paykids.data.model.allowance.MonthCategoryDTO
import com.paykids.data.model.allowance.MonthDailyDTO
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.domain.model.allowance.MonthAllCategoryInfo
import com.paykids.domain.model.allowance.MonthCategoryInfo
import com.paykids.domain.model.allowance.MonthDailyInfo

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
            id = item.id,
            date = item.date,
            allowanceType = item.allowanceType,
            category = item.category,
            amount = item.amount,
            memo = item.memo
        )
    }
}
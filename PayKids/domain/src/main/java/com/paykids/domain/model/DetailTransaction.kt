package com.paykids.domain.model

sealed class DetailTransaction {
    abstract val date: String
    abstract val category: String
    abstract val amount: Int
    abstract val memo: String
}

data class DetailConsume(
    override val date: String,
    override val category: String,
    override val amount: Int,
    override val memo: String
) : DetailTransaction()

data class DetailIncome(
    override val date: String,
    override val category: String,
    override val amount: Int,
    override val memo: String
) : DetailTransaction()
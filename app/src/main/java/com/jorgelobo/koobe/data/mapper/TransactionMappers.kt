package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.TransactionEntity
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import java.util.Date

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    date = Date(date),
    description = description,
    type = type,
    categoryId = categoryId,
    subcategoryId = subcategoryId,
    amount = amount,
    paymentMethod = paymentMethod,
    currency = currency
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    date = date.time,
    description = description,
    type = type,
    categoryId = categoryId,
    subcategoryId = subcategoryId,
    amount = amount,
    paymentMethod = paymentMethod,
    currency = currency
)
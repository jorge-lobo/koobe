package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.BudgetEntity
import com.jorgelobo.koobe.domain.model.budget.Budget

fun BudgetEntity.toDomain() = Budget(
    id = id,
    categoryId = categoryId,
    subcategoryId = subcategoryId ?: 0,
    period = period,
    repeat = repeat,
    paymentMethod = paymentMethod,
    currency = currency,
    limitAmount = limitAmount,
    spentAmount = spentAmount,
    projectedAmount = projectedAmount,
    dailyAverage = dailyAverage
)

fun Budget.toEntity() = BudgetEntity(
    id = id,
    categoryId = categoryId,
    subcategoryId = subcategoryId,
    period = period,
    repeat = repeat,
    paymentMethod = paymentMethod,
    currency = currency,
    limitAmount = limitAmount,
    spentAmount = spentAmount,
    projectedAmount = projectedAmount,
    dailyAverage = dailyAverage
)
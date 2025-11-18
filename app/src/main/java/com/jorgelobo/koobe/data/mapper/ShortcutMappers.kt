package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.ShortcutEntity
import com.jorgelobo.koobe.data.local.icon.IconResolver
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

fun ShortcutEntity.toDomain() = Shortcut(
    id = id,
    name = name,
    icon = IconResolver.resolve(iconName),
    categoryId = categoryId,
    transactionType = transactionType,
    paymentMethod = paymentMethod,
    currency = currency,
    amount = amount,
    repeat = repeat,
    period = period
)

fun Shortcut.toEntity() = ShortcutEntity(
    id = id,
    name = name,
    iconName = icon.name,
    categoryId = categoryId,
    transactionType = transactionType,
    paymentMethod = paymentMethod,
    currency = currency,
    amount = amount,
    repeat = repeat,
    period = period
)
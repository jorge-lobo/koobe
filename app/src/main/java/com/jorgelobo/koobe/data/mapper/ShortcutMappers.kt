package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.ShortcutEntity
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun ShortcutEntity.toDomain() = Shortcut(
    id = id,
    name = name,
    icon = IconPack.valueOf(iconName),
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
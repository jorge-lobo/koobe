package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.ShortcutEntity
import com.jorgelobo.koobe.domain.model.shortcut.Shortcut
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import java.util.Date

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
    period = period,
    usageCount = usageCount,
    lastExecutionDate = lastExecutionDate?.let(::Date)
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
    period = period,
    usageCount = usageCount,
    lastExecutionDate = lastExecutionDate?.time
)
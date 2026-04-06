package com.jorgelobo.koobe.domain.mappers

import com.jorgelobo.koobe.data.local.entity.CategoryEntity
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = IconPack.valueOf(iconName),
        color = color,
        type = type
    )
}
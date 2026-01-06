package com.jorgelobo.koobe.domain.mappers

import com.jorgelobo.koobe.data.local.entity.CategoryEntity
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.ui.mappers.getIconFromString

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = getIconFromString(iconName),
        color = color,
        type = type
    )
}
package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.CategoryEntity
import com.jorgelobo.koobe.data.local.icon.IconResolver
import com.jorgelobo.koobe.domain.model.category.Category

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    icon = IconResolver.resolve(iconName),
    color = color,
    type = type,
    subcategories = emptyList()
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    iconName = icon.name,
    color = color,
    type = type
)
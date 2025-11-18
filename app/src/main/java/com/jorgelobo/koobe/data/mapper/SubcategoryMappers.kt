package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.SubcategoryEntity
import com.jorgelobo.koobe.data.local.icon.IconResolver
import com.jorgelobo.koobe.domain.model.category.Subcategory

fun SubcategoryEntity.toDomain() = Subcategory(
    id = id,
    categoryId = categoryId,
    name = name,
    icon = IconResolver.resolve(iconName)
)

fun Subcategory.toEntity() = SubcategoryEntity(
    id = id,
    categoryId = categoryId,
    name = name,
    iconName = icon.name
)
package com.jorgelobo.koobe.data.mapper

import com.jorgelobo.koobe.data.local.entity.SubcategoryEntity
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun SubcategoryEntity.toDomain() = Subcategory(
    id = id,
    categoryId = categoryId,
    name = name,
    icon = IconPack.valueOf(iconName)
)

fun Subcategory.toEntity() = SubcategoryEntity(
    id = id,
    categoryId = categoryId,
    name = name,
    iconName = icon.name
)
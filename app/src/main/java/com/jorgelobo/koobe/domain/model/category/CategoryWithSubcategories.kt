package com.jorgelobo.koobe.domain.model.category

import androidx.room.Embedded
import androidx.room.Relation
import com.jorgelobo.koobe.data.local.entity.CategoryEntity
import com.jorgelobo.koobe.data.local.entity.SubcategoryEntity

data class CategoryWithSubcategories(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val subcategories: List<SubcategoryEntity>
)
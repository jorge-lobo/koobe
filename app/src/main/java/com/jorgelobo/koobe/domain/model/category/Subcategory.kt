package com.jorgelobo.koobe.domain.model.category

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

data class Subcategory(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: ImageVector
) {

    companion object {
        fun empty(): Subcategory = Subcategory(
            id = -1,
            categoryId = -1,
            name = "",
            icon = IconGeneral.EMPTY.icon,
        )
    }
}
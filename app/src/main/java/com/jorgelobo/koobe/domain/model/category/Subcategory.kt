package com.jorgelobo.koobe.domain.model.category

import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class Subcategory(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: IconPack
) {

    companion object {
        fun empty(): Subcategory = Subcategory(
            id = 0,
            categoryId = 0,
            name = "",
            icon = IconPack.PLACEHOLDER,
        )
    }
}
package com.jorgelobo.koobe.domain.model.category

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.interfaces.HasColor
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class Category(
    val id: Int,
    val name: String,
    val icon: IconPack,
    override val color: String,
    val type: TransactionType,
    val subcategories: List<Subcategory> = emptyList()
) : HasColor {

    companion object {
        fun empty(): Category = Category(
            id = 0,
            name = "",
            icon = IconPack.PLACEHOLDER,
            color = "",
            type = TransactionType.EXPENSE,
            subcategories = emptyList()
        )
    }
}
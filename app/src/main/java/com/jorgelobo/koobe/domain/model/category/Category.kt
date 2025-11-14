package com.jorgelobo.koobe.domain.model.category

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.interfaces.HasColor

data class Category(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    override val color: String,
    val type: TransactionType,
    val subcategories: List<Subcategory> = emptyList()
) : HasColor
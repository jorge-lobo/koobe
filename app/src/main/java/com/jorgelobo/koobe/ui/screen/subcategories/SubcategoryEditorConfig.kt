package com.jorgelobo.koobe.ui.screen.subcategories

import kotlinx.serialization.Serializable

@Serializable
data class SubcategoryEditorConfig(
    val subcategoryId: Int? = null,
    val categoryId: Int? = null
) {
    val isEditMode: Boolean
        get() = subcategoryId != null
}
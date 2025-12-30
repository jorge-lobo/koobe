package com.jorgelobo.koobe.ui.screen.categories.editor

import kotlinx.serialization.Serializable

@Serializable
data class CategoryEditorConfig(
    val categoryId: Int? = null
) {
    val isEditMode: Boolean
        get() = categoryId != null
}
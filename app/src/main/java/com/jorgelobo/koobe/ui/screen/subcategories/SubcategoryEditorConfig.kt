package com.jorgelobo.koobe.ui.screen.subcategories

import kotlinx.serialization.Serializable

/**
 * Configuration for the Subcategory Editor screen.
 *
 * Determines whether the screen is in create or edit mode.
 */
@Serializable
data class SubcategoryEditorConfig(
    val subcategoryId: Int? = null,
    val categoryId: Int? = null
) {
    /**
     * True when editing an existing subcategory.
     */
    val isEditMode: Boolean
        get() = subcategoryId != null
}
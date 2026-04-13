package com.jorgelobo.koobe.ui.screen.subcategories.state

import com.jorgelobo.koobe.ui.components.model.icons.IconPack

/**
 * UI form state for subcategory editing/creation.
 */
data class SubcategoryFormState(
    val name: String? = null,
    val icon: IconPack? = null,
    val categoryId: Int? = null,
)
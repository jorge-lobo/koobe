package com.jorgelobo.koobe.ui.screen.categories.selector

import kotlinx.serialization.Serializable

@Serializable
enum class CategorySelectorMode {
    CREATE_TRANSACTION,
    EDIT_TRANSACTION,
    EDIT_SUBCATEGORY,
    CREATE_SHORTCUT,
    EDIT_SHORTCUT,
    EDIT_BUDGET
}
package com.jorgelobo.koobe.ui.screen.categories.selector

import kotlinx.serialization.Serializable

@Serializable
enum class CategorySelectorTarget {
    TRANSACTION_EDITOR,
    SHORTCUT_EDITOR,
    SUBCATEGORY_EDITOR,
    BUDGET_EDITOR
}
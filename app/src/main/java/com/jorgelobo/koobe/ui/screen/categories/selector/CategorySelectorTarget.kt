package com.jorgelobo.koobe.ui.screen.categories.selector

import kotlinx.serialization.Serializable

/**
 * Represents the different screens that can launch the category selector and defines where
 * the selection result should be delivered.
 *
 * This value is resolved into a concrete navigation destination once the selection process
 * is completed.
 */
@Serializable
enum class CategorySelectorTarget {
    TRANSACTION_EDITOR,
    SHORTCUT_EDITOR,
    SUBCATEGORY_EDITOR,
    BUDGET_EDITOR
}
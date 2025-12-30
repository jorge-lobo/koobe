package com.jorgelobo.koobe.ui.screen.budgets.editor

import kotlinx.serialization.Serializable

@Serializable
data class BudgetEditorConfig(
    val budgetId: Int? = null,
    val categoryId: Int? = null,
    val subcategoryId: Int? = null
) {
    val isEditMode: Boolean
        get() = budgetId != null
}
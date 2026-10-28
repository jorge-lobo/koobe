package com.jorgelobo.koobe.ui.screen.budgets.editor

import kotlinx.serialization.Serializable

@Serializable
sealed interface BudgetEditorConfig {

    @Serializable
    data class Create(
        val categoryId: Int,
        val subcategoryId: Int
    ) : BudgetEditorConfig

    @Serializable
    data class Edit(
        val budgetId: Int
    ) : BudgetEditorConfig
}
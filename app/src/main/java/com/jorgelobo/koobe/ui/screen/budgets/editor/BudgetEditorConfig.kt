package com.jorgelobo.koobe.ui.screen.budgets.editor

import kotlinx.serialization.Serializable

/**
 * Configuration for the Budget Editor screen, defining the parameters required to either create
 * a new budget or edit an existing one.
 */
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
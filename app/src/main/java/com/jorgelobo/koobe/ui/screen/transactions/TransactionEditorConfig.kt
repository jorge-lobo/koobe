package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import kotlinx.serialization.Serializable

/**
 * Configuration for [TransactionEditorViewModel] and the transaction editor screen.
 *
 * Holds all IDs and parameters needed to initialize the editor, including:
 * - The transaction being edited (if any)
 * - The associated category, subcategory, or shortcut
 * - The transaction type (e.g., expense or income)
 * - The origin route for navigation purposes
 *
 * @property transactionId The ID of the transaction being edited, or null for new transactions.
 * @property categoryId The ID of the selected category.
 * @property subcategoryId Optional ID of the selected subcategory.
 * @property shortcutId Optional ID of the selected shortcut.
 * @property transactionType Initial transaction type; defaults to [TransactionType.EXPENSE].
 * @property originRoute The route to return to after editing is complete.
 *
 * @property isEditMode True if the editor is in edit mode (i.e., editing an existing transaction).
 */
@Serializable
data class TransactionEditorConfig(
    val transactionId: Int? = null,
    val categoryId: Int,
    val subcategoryId: Int? = null,
    val shortcutId: Int? = null,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val originRoute: String
) {
    val isEditMode: Boolean
        get() = transactionId != null
}
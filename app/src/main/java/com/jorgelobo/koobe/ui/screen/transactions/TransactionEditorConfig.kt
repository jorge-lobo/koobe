package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import kotlinx.serialization.Serializable

/**
 * Configuration for the Transaction Editor screen.
 *
 * @property transactionId Transaction identifier. When `null`, the editor operates in create mode.
 * @property categoryId Selected category identifier.
 * @property subcategoryId Selected subcategory identifier, if any.
 * @property shortcutId Shortcut identifier used to prefill the editor, if any.
 * @property transactionType Transaction type.
 * @property originRoute Route from which the editor was opened.
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
    /** `true` when editing an existing transaction; `false` when creating a new one. */
    val isEditMode: Boolean
        get() = transactionId != null
}
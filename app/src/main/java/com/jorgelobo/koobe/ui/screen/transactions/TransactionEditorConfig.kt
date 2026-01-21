package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import kotlinx.serialization.Serializable

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
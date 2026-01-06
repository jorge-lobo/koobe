package com.jorgelobo.koobe.ui.screen.transactions

import kotlinx.serialization.Serializable

@Serializable
data class TransactionEditorConfig(
    val transactionId: Int? = null,
    val categoryId: Int? = null,
    val subcategoryId: Int? = null,
    val shortcutId: Int? = null
) {
    val isEditMode: Boolean
        get() = transactionId != null
}
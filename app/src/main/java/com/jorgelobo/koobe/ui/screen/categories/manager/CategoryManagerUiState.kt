package com.jorgelobo.koobe.ui.screen.categories.manager

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

data class CategoryManagerUiState(
    val transactionTypeSelected: TransactionType = TransactionType.EXPENSE,
    val categories: List<CategoryItemUi> = emptyList(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
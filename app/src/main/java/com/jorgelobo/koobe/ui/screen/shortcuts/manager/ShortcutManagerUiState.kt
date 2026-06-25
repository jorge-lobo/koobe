package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

data class ShortcutManagerUiState(
    val transactionTypeSelected: TransactionType = TransactionType.EXPENSE,
    val shortcutItems: List<Shortcut> = emptyList(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
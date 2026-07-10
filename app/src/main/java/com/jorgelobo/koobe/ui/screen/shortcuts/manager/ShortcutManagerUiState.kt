package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

data class ShortcutManagerUiState(
    val transactionTypeSelected: TransactionType = TransactionType.EXPENSE,
    val shortcutItems: List<ShortcutItemUi> = emptyList(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val sortingSelector: SelectorSheetState<SortingType> = SelectorSheetState(
        false,
        SortingType.NAME_ASC
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
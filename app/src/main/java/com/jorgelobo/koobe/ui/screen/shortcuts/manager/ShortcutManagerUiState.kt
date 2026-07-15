package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

/**
 * Represents the UI state for the Shortcut Manager screen.
 *
 * @property transactionTypeSelected The currently selected [TransactionType] to filter the shortcuts.
 * @property shortcutItems The list of shortcut items to be displayed in the manager.
 * @property deleteDialog The state of the confirmation dialog used for deleting a shortcut.
 * @property sortingSelector The state of the selector sheet used for choosing the [SortingType].
 * @property isLoading Indicates whether the screen is currently performing a loading operation.
 * @property errorMessage An optional error message to be displayed if an operation fails.
 */
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
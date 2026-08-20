package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence.ShortcutRecurrenceBottomSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

/**
 * Represents the UI state for the Shortcut Manager screen.
 *
 * @property transactionTypeSelected The currently selected [TransactionType] to filter the shortcuts.
 * @property shortcutItems The list of shortcut items to be displayed in the manager.
 * @property shortcutRecurrenceTarget The shortcut for which the recurrence is being modified.
 * @property deleteDialog The state of the confirmation dialog used for deleting a shortcut.
 * @property shortcutRecurrenceSheet The state of the shortcut recurrence bottom sheet.
 * @property periodSelector The state of the selector sheet used for choosing the [PeriodType].
 * @property sortingSelector The state of the selector sheet used for choosing the [SortingType].
 * @property isLoading Indicates whether the screen is currently performing a loading operation.
 * @property errorMessage An optional error message to be displayed if an operation fails.
 */
data class ShortcutManagerUiState(
    val transactionTypeSelected: TransactionType = TransactionType.EXPENSE,
    val shortcutItems: List<ShortcutItemUi> = emptyList(),
    val shortcutRecurrenceTarget: Shortcut? = null,
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val shortcutRecurrenceSheet: ShortcutRecurrenceBottomSheetState = ShortcutRecurrenceBottomSheetState.Hidden,
    val periodSelector: SelectorSheetState<PeriodType> = SelectorSheetState(
        visible = false,
        selected = PeriodType.DAILY
    ),
    val sortingSelector: SelectorSheetState<SortingType> = SelectorSheetState(
        visible = false,
        selected = SortingType.NAME_ASC
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
package com.jorgelobo.koobe.ui.screen.shortcuts.editor.state

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

/**
 * Internal UI state for the Shortcut Editor screen.
 *
 * This class holds the state of various UI components and operation flags that are not part of
 * the core domain model but are necessary for managing the screen's behavior.
 *
 * @property discardDialog State for the confirmation dialog shown when discarding changes.
 * @property deleteDialog State for the confirmation dialog shown when deleting a shortcut.
 * @property iconSelectDialog State for the dialog used to select a shortcut icon.
 * @property currencyDialog State for the dialog used to select a currency type.
 * @property paymentMethodSelector State for the bottom sheet used to select a payment method.
 * @property periodSelector State for the bottom sheet used to select a period type.
 * @property isSaving Indicates whether a save operation is currently in progress.
 * @property isDeleting Indicates whether a delete operation is currently in progress.
 * @property hasTriedToSave Indicates if the user has attempted to save the form, used to trigger
 * validation error visibility.
 */
data class ShortcutUiStateInternal(
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val iconSelectDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(),
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType> =
        SelectorSheetState(selected = PaymentMethodType.CASH),
    val periodSelector: SelectorSheetState<PeriodType> =
        SelectorSheetState(selected = PeriodType.DAILY),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val hasTriedToSave: Boolean = false,
    val nameError: NameValidationException? = null
)
package com.jorgelobo.koobe.ui.screen.shortcuts.editor.state

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

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
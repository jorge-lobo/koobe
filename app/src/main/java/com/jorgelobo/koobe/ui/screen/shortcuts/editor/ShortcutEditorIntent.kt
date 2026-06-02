package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

sealed interface ShortcutEditorIntent {
    sealed interface State : ShortcutEditorIntent {
        data class NameChanged(val name: String) : State
        data class IconChanged(val icon: IconPack) : State
        data class AmountKeyPressed(val key: KeypadKey) : State
        data class CurrencyChanged(val currency: CurrencyType) : State
        data class PaymentMethodChanged(val paymentMethod: PaymentMethodType) : State
        data class RepeatChanged(val repeat: Boolean) : State
        data class RepeatPeriodChanged(val period: PeriodType) : State

        data object AmountResetClicked : State
    }

    sealed interface Action : ShortcutEditorIntent {
        data object SaveClicked : Action
        data object CloseClicked : Action
        data object RequestDeleteShortcut : Action
        data object ChangeCategoryClicked : Action

        data class DiscardDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class DeleteDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class IconSelectDialogUpdated(val action: SelectorDialogAction<IconPack>) : Action
        data class CurrencyDialogUpdated(val action: SelectorDialogAction<CurrencyType>) : Action
        data class PeriodSelectorUpdated(val action: SelectorSheetAction<PeriodType>) : Action
        data class PaymentMethodSelectorUpdated(val action: SelectorSheetAction<PaymentMethodType>) :
            Action
    }
}
package com.jorgelobo.koobe.ui.screen.budgets.editor

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

sealed interface BudgetEditorIntent {

    sealed interface State : BudgetEditorIntent {
        data class AmountKeyPressed(val key: KeypadKey) : State
        data class PeriodChanged(val period: PeriodType) : State
        data class RepeatChanged(val repeat: Boolean) : State

        data object AmountResetClicked : State
    }

    sealed interface Action : BudgetEditorIntent {
        data object SaveClicked : Action
        data object CloseClicked : Action
        data object RequestDeleteBudget : Action
        data object ChangeCategoryClicked : Action

        data class DiscardDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class DeleteDialogUpdated(val action: ConfirmationDialogAction) : Action
    }
}
package com.jorgelobo.koobe.ui.screen.budgets.editor

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

sealed interface BudgetEditorIntent {

    // Form field updates — handled synchronously by the Reducer.
    sealed interface State : BudgetEditorIntent {
        data class AmountKeyPressed(val key: KeypadKey) : State
        data class PeriodChanged(val period: PeriodType) : State
        data class RepeatChanged(val repeat: Boolean) : State

        data object AmountResetClicked : State
    }

    // User actions — may trigger coroutines, navigation, or dialog side effects.
    sealed interface Action : BudgetEditorIntent {
        data object SaveClicked : Action
        data object CloseClicked : Action
        data object RequestDeleteBudget : Action
        data object ChangeCategoryClicked : Action
        data object ContinueCreatingBudget : Action
        data object CancelExistingBudgetsDialog : Action

        data class DiscardDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class DeleteDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class DuplicateBudgetDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class EditExistingBudget(val budgetId: Int) : Action
    }
}
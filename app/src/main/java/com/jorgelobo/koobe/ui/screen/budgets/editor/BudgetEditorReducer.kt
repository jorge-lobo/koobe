package com.jorgelobo.koobe.ui.screen.budgets.editor

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.core.model.updateIfChanged
import com.jorgelobo.koobe.domain.amount.reduceAmountInput
import com.jorgelobo.koobe.ui.mappers.toAmountAction
import com.jorgelobo.koobe.ui.screen.budgets.editor.state.BudgetFormState
import com.jorgelobo.koobe.ui.screen.budgets.editor.state.BudgetUiStateInternal

object BudgetEditorReducer {

    data class Result(
        val form: BudgetFormState,
        val internal: BudgetUiStateInternal
    )

    fun reduce(
        intent: BudgetEditorIntent.State,
        currentForm: BudgetFormState,
        currentInternal: BudgetUiStateInternal,
        baseState: BudgetEditorUiState
    ): Result {
        return when (intent) {

            is BudgetEditorIntent.State.PeriodChanged -> {
                Result(
                    form = currentForm.copy(
                        period = updateIfChanged(
                            intent.period,
                            baseState.period
                        )
                    ),
                    internal = currentInternal
                )
            }

            is BudgetEditorIntent.State.RepeatChanged -> {
                Result(
                    form = currentForm.copy(
                        isRepeat = updateIfChanged(
                            intent.repeat,
                            baseState.isRepeat
                        )
                    ),
                    internal = currentInternal
                )
            }

            is BudgetEditorIntent.State.AmountKeyPressed -> {
                val isEditMode = baseState.config?.isEditMode == true
                val shouldReset = isEditMode && !currentForm.amountKeypadTouched
                val currentLimit =
                    if (shouldReset) "0"
                    else currentForm.limitInput.resolve(baseState.limitAmountInput)
                val updatedLimit = reduceAmountInput(
                    currentLimit,
                    intent.key.toAmountAction()
                )

                Result(
                    form = currentForm.copy(
                        limitInput = FieldUpdate.Updated(updatedLimit),
                        amountKeypadTouched = true
                    ),
                    internal = currentInternal
                )
            }

            is BudgetEditorIntent.State.AmountResetClicked -> {
                Result(
                    form = currentForm.copy(
                        limitInput = FieldUpdate.Updated("0"),
                        amountKeypadTouched = true
                    ),
                    internal = currentInternal
                )
            }
        }
    }
}
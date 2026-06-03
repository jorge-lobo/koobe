package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.core.model.updateIfChanged
import com.jorgelobo.koobe.domain.amount.reduceAmountInput
import com.jorgelobo.koobe.ui.mappers.toAmountAction
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutFormState
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.state.ShortcutUiStateInternal

object ShortcutEditorReducer {
    data class Result(
        val form: ShortcutFormState,
        val internal: ShortcutUiStateInternal
    )

    fun reduce(
        intent: ShortcutEditorIntent.State,
        currentForm: ShortcutFormState,
        currentInternal: ShortcutUiStateInternal,
        baseState: ShortcutEditorUiState
    ): Result {
        return when (intent) {
            is ShortcutEditorIntent.State.NameChanged -> {
                Result(
                    form = currentForm.copy(
                        name = updateIfChanged(
                            intent.name,
                            baseState.name
                        )
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.AmountKeyPressed -> {
                val isEditMode = baseState.config?.isEditMode == true
                val shouldReset = isEditMode && !currentForm.amountKeypadTouched
                val currentAmount =
                    if (shouldReset) "0"
                    else currentForm.amountInput.resolve(baseState.amountInput)
                val updatedAmount = reduceAmountInput(
                    currentAmount,
                    intent.key.toAmountAction()
                )

                Result(
                    form = currentForm.copy(
                        amountInput = FieldUpdate.Updated(updatedAmount),
                        amountKeypadTouched = true
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.CurrencyChanged -> {
                Result(
                    form = currentForm.copy(
                        currency = updateIfChanged(
                            intent.currency,
                            baseState.currencyType
                        )
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.IconChanged -> {
                Result(
                    form = currentForm.copy(
                        icon = updateIfChanged(
                            intent.icon,
                            baseState.icon
                        )
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.PaymentMethodChanged -> {
                Result(
                    form = currentForm.copy(
                        paymentMethod = updateIfChanged(
                            intent.paymentMethod,
                            baseState.paymentMethodType
                        )
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.RepeatChanged -> {
                Result(
                    form = currentForm.copy(
                        repeat = updateIfChanged(
                            intent.repeat,
                            baseState.repeat
                        )
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.RepeatPeriodChanged -> {
                Result(
                    form = currentForm.copy(
                        repeatFrequency = updateIfChanged(
                            intent.period,
                            baseState.repeatFrequency
                        )
                    ),
                    internal = currentInternal
                )
            }

            is ShortcutEditorIntent.State.AmountResetClicked -> {
                Result(
                    form = currentForm.copy(
                        amountInput = FieldUpdate.Updated("0"),
                        amountKeypadTouched = true
                    ),
                    internal = currentInternal
                )
            }
        }
    }
}
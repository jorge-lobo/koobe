package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.core.model.updateIfChanged
import com.jorgelobo.koobe.domain.amount.reduceAmountInput
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.ui.mappers.toAmountAction
import com.jorgelobo.koobe.ui.screen.transactions.state.TransactionFormState
import com.jorgelobo.koobe.ui.screen.transactions.state.TransactionUiStateInternal

/**
 * Pure reducer responsible for transforming Transaction Editor state based on State intents.
 *
 * This reducer takes the current form and internal state and produces a new immutable state
 * without side effects.
 */
object TransactionEditorReducer {

    /**
     * Result of a reduction, containing updated form and internal UI state.
     *
     * @property form Updated form state.
     * @property internal Updated internal UI state.
     */
    data class Result(
        val form: TransactionFormState,
        val internal: TransactionUiStateInternal
    )

    /**
     * Applies a [TransactionEditorIntent.State] to the current state and returns a new state.
     *
     * @param intent User intent representing a state change.
     * @param currentForm Current form state before reduction.
     * @param currentInternal Current internal UI state before reduction.
     * @param baseState Base UI state used for reference comparisons and defaults.
     */
    fun reduce(
        intent: TransactionEditorIntent.State,
        currentForm: TransactionFormState,
        currentInternal: TransactionUiStateInternal,
        baseState: TransactionEditorUiState,
    ): Result {
        return when (intent) {

            is TransactionEditorIntent.State.DescriptionInputChanged -> {
                Result(
                    form = currentForm.copy(
                        description = updateIfChanged(
                            DescriptionSource.TextDescription(intent.description),
                            baseState.descriptionSource
                        )
                    ),
                    internal = currentInternal
                )
            }

            is TransactionEditorIntent.State.AmountKeyPressed -> {
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

            is TransactionEditorIntent.State.DateChanged -> {
                Result(
                    form = currentForm.copy(
                        date = updateIfChanged(
                            intent.date,
                            baseState.date
                        )
                    ),
                    internal = currentInternal
                )
            }

            is TransactionEditorIntent.State.CurrencyChanged -> {
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

            is TransactionEditorIntent.State.PaymentMethodChanged -> {
                Result(
                    form = currentForm.copy(
                        paymentMethod = updateIfChanged(
                            intent.method,
                            baseState.paymentMethodType
                        )
                    ),
                    internal = currentInternal
                )
            }

            is TransactionEditorIntent.State.AmountResetClicked -> {
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
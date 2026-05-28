package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

/**
 * Represents the complete UI state of the Transaction Editor screen.
 *
 * This state aggregates all user-editable fields, selected entities, dialog states, and derived
 * properties required to render and manage the screen.
 *
 * @property config Editor configuration defining mode and navigation context.
 * @property originalTransaction Original transaction when editing an existing entry.
 * @property category Selected category.
 * @property subcategory Selected subcategory, if any.
 * @property shortcut Optional shortcut used to prefill the form.
 * @property descriptionSource Source of the transaction description.
 * @property inputState Input validation and UI state for text fields.
 * @property date Selected transaction date.
 * @property language Current UI language used for date formatting.
 * @property paymentMethodType Selected payment method.
 * @property currencyType Selected currency.
 * @property amountInput Raw amount input as string (keypad representation).
 * @property amount Parsed numeric amount.
 * @property isLoading Indicates whether the screen is in a loading state.
 * @property errorMessage Optional error message to display.
 * @property transactionInitialSnapshot Snapshot used to detect changes in edit mode.
 * @property showSnackBar Controls snackbar visibility trigger.
 * @property discardDialog State of discard confirmation dialog.
 * @property deleteDialog State of delete confirmation dialog.
 * @property currencyDialog State of currency selector dialog.
 * @property paymentMethodSelector State of payment method selector bottom sheet.
 * @property datePickerDialog State of date picker dialog.
 */
data class TransactionEditorUiState(
    val config: TransactionEditorConfig? = null,
    val originalTransaction: Transaction? = null,
    val category: Category,
    val subcategory: Subcategory? = null,
    val shortcut: Shortcut? = null,
    val descriptionSource: DescriptionSource = DescriptionSource.Empty,
    val inputState: InputState,
    val date: Date = DateUtils.currentDate,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val paymentMethodType: PaymentMethodType = PaymentMethodType.CASH,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val amountInput: String = "0",
    val amount: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val transactionInitialSnapshot: TransactionInitialSnapshot,
    val showSnackBar: Boolean = false,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(),
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType>,
    val datePickerDialog: DatePickerDialogState = DatePickerDialogState(
        visible = false,
        selectedDate = DateUtils.currentDate,
        language = language
    )
) {

    /** Returns `true` if the current state represents a valid transaction. */
    val isValid: Boolean
        get() = category.id > 0 && amount > 0

    /**
     * Indicates whether the save action should be enabled.
     *
     * In edit mode, returns `true` only if at least one field differs from the initial snapshot.
     * In create mode, returns `true` as long as the state is valid.
     */
    val isSaveEnabled: Boolean
        get() {
            val config = config ?: return false

            if (!isValid) return false

            return if (config.isEditMode) {
                val initial = transactionInitialSnapshot

                category.id != initial.category.id ||
                        subcategory?.id != initial.subcategory?.id ||
                        shortcut?.id != initial.shortcut?.id ||
                        descriptionSource != initial.descriptionSource ||
                        !DateUtils.isSameDay(date, initial.date) ||
                        paymentMethodType != initial.paymentMethodType ||
                        currencyType != initial.currencyType ||
                        amount != initial.amount
            } else {
                true
            }
        }

    /**
     * Returns the string resource ID for the screen headline based on mode and transaction type.
     *
     * @param isEditMode Whether the editor is in edit mode.
     * @param transactionType The type of transaction (Expense or Income).
     */
    fun headlineRes(isEditMode: Boolean, transactionType: TransactionType): Int =
        if (isEditMode) {
            when (transactionType) {
                TransactionType.EXPENSE -> R.string.headline_expense_editor
                TransactionType.INCOME -> R.string.headline_income_editor
            }
        } else {
            when (transactionType) {
                TransactionType.EXPENSE -> R.string.headline_expense_creator
                TransactionType.INCOME -> R.string.headline_income_creator
            }
        }

    companion object {

        /** Returns an initial loading state with default values. */
        fun initialEmpty(): TransactionEditorUiState {
            val emptyCategory = Category.empty()

            return TransactionEditorUiState(
                category = emptyCategory,
                originalTransaction = null,
                inputState = InputState.DEFAULT,
                isLoading = true,
                transactionInitialSnapshot = TransactionInitialSnapshot(
                    category = emptyCategory,
                    subcategory = null,
                    shortcut = null,
                    transactionType = TransactionType.EXPENSE,
                    descriptionSource = DescriptionSource.Empty,
                    date = DateUtils.currentDate,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR,
                    amount = 0.0
                ),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                )
            )
        }

        /** Returns an initial state for creating a new transaction. */
        fun initial(
            config: TransactionEditorConfig,
            category: Category,
            subcategory: Subcategory?,
            shortcut: Shortcut?
        ): TransactionEditorUiState {

            return TransactionEditorUiState(
                config = config,
                originalTransaction = null,
                category = category,
                subcategory = subcategory,
                shortcut = shortcut,
                descriptionSource = DescriptionSource.Empty,
                inputState = InputState.DEFAULT,
                transactionInitialSnapshot = TransactionInitialSnapshot(
                    category = category,
                    subcategory = subcategory,
                    shortcut = shortcut,
                    transactionType = config.transactionType,
                    descriptionSource = DescriptionSource.Empty,
                    date = DateUtils.currentDate,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR,
                    amount = 0.0
                ),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                )
            )
        }

        /** Returns an initial state populated from an existing transaction (edit mode). */
        fun initialFromTransaction(
            config: TransactionEditorConfig,
            transaction: Transaction,
            category: Category,
            subcategory: Subcategory?,
            shortcut: Shortcut?
        ): TransactionEditorUiState {

            val descriptionSource = DescriptionSource.TextDescription(transaction.description)

            return TransactionEditorUiState(
                config = config,
                originalTransaction = transaction,
                category = category,
                subcategory = subcategory,
                shortcut = shortcut,
                descriptionSource = descriptionSource,
                inputState = InputState.DEFAULT,
                date = transaction.date,
                paymentMethodType = transaction.paymentMethod,
                currencyType = transaction.currency,
                amount = transaction.amount,
                amountInput = transaction.amount.toString(),
                transactionInitialSnapshot = TransactionInitialSnapshot(
                    category = category,
                    subcategory = subcategory,
                    shortcut = shortcut,
                    transactionType = config.transactionType,
                    descriptionSource = descriptionSource,
                    date = transaction.date,
                    paymentMethodType = transaction.paymentMethod,
                    currencyType = transaction.currency,
                    amount = transaction.amount
                ),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = transaction.paymentMethod
                ),
                datePickerDialog = DatePickerDialogState(
                    visible = false,
                    selectedDate = transaction.date,
                    language = AppLanguage.ENGLISH
                )
            )
        }
    }
}

/**
 * Snapshot of the initial transaction state used to detect unsaved changes in edit mode.
 *
 * This immutable structure is used as a reference baseline for comparison with the current state.
 */
data class TransactionInitialSnapshot(
    val category: Category,
    val subcategory: Subcategory?,
    val shortcut: Shortcut?,
    val transactionType: TransactionType,
    val descriptionSource: DescriptionSource,
    val date: Date,
    val paymentMethodType: PaymentMethodType,
    val currencyType: CurrencyType,
    val amount: Double
)
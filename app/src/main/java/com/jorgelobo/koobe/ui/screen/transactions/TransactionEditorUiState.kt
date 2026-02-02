package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.utils.DateUtils
import java.util.Date

/**
 * Represents the entire UI state of the Transaction Editor screen.
 *
 * This state holds all the information necessary to render the editor, including:
 * - Selected category, subcategory, and shortcut
 * - Transaction details (date, amount, currency, payment method, description)
 * - Dialogs and bottom sheets states (discard confirmation, currency selector, payment method
 * selector, date picker)
 * - Input state and loading/error indicators
 * - Snapshot of initial values for unsaved changes detection
 *
 * The state is immutable; updates should be performed via [TransactionEditorViewModel].
 *
 * @property config The optional configuration for the editor (edit mode, IDs, origin route, etc.).
 * @property category The currently selected category.
 * @property subcategory The currently selected subcategory, if any.
 * @property shortcut The currently selected shortcut, if any.
 * @property descriptionSource The current source of the transaction description (text or empty).
 * @property inputState The current input state (e.g., default, editing).
 * @property date The transaction date.
 * @property paymentMethodType The selected payment method.
 * @property currencyType The selected currency.
 * @property amountInput The raw string input for the amount.
 * @property amount The parsed amount as a Double.
 * @property isSaveButtonEnabled Whether the Save button should be enabled.
 * @property isLoading Whether the screen is currently loading data.
 * @property errorMessage An optional error message to display.
 * @property initialSnapshot Snapshot of the initial state to detect unsaved changes.
 * @property showSnackBar Whether a snackBar should be shown.
 * @property discardDialog State of the discard confirmation dialog.
 * @property currencyDialog State of the currency selection dialog.
 * @property paymentMethodSelector State of the payment method selection sheet.
 * @property datePickerDialog State of the date picker dialog.
 *
 * @property hasUnsavedChanges Returns true if any editable field differs from the initial snapshot.
 *
 * @function headlineRes Returns the string resource for the screen headline depending on edit mode
 * and transaction type.
 *
 * @companion object Provides factory methods to generate initial UI states:
 * - [initialEmpty]: A loading/empty state for first initialization
 * - [initial]: A fully initialized state with provided category/subcategory/shortcut
 */
data class TransactionEditorUiState(
    val config: TransactionEditorConfig? = null,
    val category: Category,
    val subcategory: Subcategory? = null,
    val shortcut: Shortcut? = null,
    val descriptionSource: DescriptionSource? = null,
    val inputState: InputState,
    val date: Date = DateUtils.currentDate,
    val paymentMethodType: PaymentMethodType = PaymentMethodType.CASH,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val amountInput: String = "0",
    val amount: Double = 0.0,
    val isSaveButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val initialSnapshot: InitialSnapshot,
    val showSnackBar: Boolean = false,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(initial = CurrencyType.EUR),
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType>,
    val datePickerDialog: DatePickerDialogState = DatePickerDialogState(
        visible = false,
        selectedDate = DateUtils.currentDate
    )
) {
    /**
     * Checks if any field in the UI state differs from the initial snapshot.
     * Used to determine if the Save button should be enabled or if discard confirmation is needed.
     */
    val hasUnsavedChanges: Boolean
        get() = category.id != initialSnapshot.category.id ||
                subcategory?.id != initialSnapshot.subcategory?.id ||
                shortcut?.id != initialSnapshot.shortcut?.id ||
                descriptionSource != initialSnapshot.descriptionSource ||
                !DateUtils.isSameDay(date, initialSnapshot.date) ||
                paymentMethodType != initialSnapshot.paymentMethodType ||
                currencyType != initialSnapshot.currencyType ||
                amount != initialSnapshot.amount

    /**
     * Returns the string resource ID for the screen headline based on edit mode and transaction type.
     *
     * @param isEditMode Whether the editor is in edit mode.
     * @param transactionType The type of the transaction (Expense or Income).
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

        /**
         * Returns an empty/loading UI state with default values.
         */
        fun initialEmpty(): TransactionEditorUiState {
            val emptyCategory = Category.empty()

            return TransactionEditorUiState(
                category = emptyCategory,
                inputState = InputState.DEFAULT,
                isLoading = true,
                initialSnapshot = InitialSnapshot(
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

        /**
         * Returns a fully initialized UI state with provided configuration and preselected items.
         */
        fun initial(
            config: TransactionEditorConfig,
            category: Category,
            subcategory: Subcategory?,
            shortcut: Shortcut?
        ): TransactionEditorUiState {

            return TransactionEditorUiState(
                category = category,
                subcategory = subcategory,
                shortcut = shortcut,
                descriptionSource = DescriptionSource.Empty,
                inputState = InputState.DEFAULT,
                initialSnapshot = InitialSnapshot(
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
    }
}

/**
 * Snapshot of the initial transaction state used to detect unsaved changes.
 */
data class InitialSnapshot(
    val category: Category,
    val subcategory: Subcategory?,
    val shortcut: Shortcut?,
    val transactionType: TransactionType,
    val descriptionSource: DescriptionSource,
    val date: Date,
    val paymentMethodType: PaymentMethodType,
    val currencyType: CurrencyType,
    val amount: Double,
)
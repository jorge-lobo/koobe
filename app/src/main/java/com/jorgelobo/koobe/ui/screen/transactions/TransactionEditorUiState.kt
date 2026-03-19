package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
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
 * Represents the UI state for the Transaction Editor screen.
 *
 * This state manages all data required to render the transaction creation or editing interface,
 * including selection data (category, subcategory, shortcut), transaction details (amount,
 * date, currency, payment method), and the visibility/state of various UI components
 * like dialogs and bottom sheets.
 *
 * The state is immutable and serves as a single source of truth for the view.
 * Changes are driven by the ViewModel.
 *
 * @property config Configuration details for the editor, such as IDs and edit mode status.
 * @property category The currently selected [Category].
 * @property subcategory The currently selected [Subcategory], if applicable.
 * @property shortcut The currently selected [Shortcut], if applicable.
 * @property descriptionSource The current source providing the transaction description.
 * @property inputState The state of the numeric input (e.g., whether the user is typing).
 * @property date The selected date for the transaction.
 * @property language The current [AppLanguage] used for localized UI elements like date pickers.
 * @property paymentMethodType The selected [PaymentMethodType].
 * @property currencyType The selected [CurrencyType].
 * @property amountInput The raw string representation of the amount entered by the user.
 * @property amount The parsed numeric value of the transaction.
 * @property isSaveButtonEnabled Indicates if the current input is valid for saving.
 * @property isLoading Indicates if the screen is currently fetching initial data.
 * @property errorMessage An optional error message to be displayed in the UI.
 * @property initialSnapshot A snapshot of the state at initialization used for change detection.
 * @property showSnackBar Triggers the display of a snackBar message.
 * @property discardDialog State for the "discard changes" confirmation dialog.
 * @property currencyDialog State for the currency selection dialog.
 * @property paymentMethodSelector State for the payment method bottom sheet selector.
 * @property datePickerDialog State for the date picker dialog.
 */
data class TransactionEditorUiState(
    val config: TransactionEditorConfig? = null,
    val category: Category,
    val subcategory: Subcategory? = null,
    val shortcut: Shortcut? = null,
    val descriptionSource: DescriptionSource? = null,
    val inputState: InputState,
    val date: Date = DateUtils.currentDate,
    val language: AppLanguage = AppLanguage.ENGLISH,
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
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(),
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType>,
    val datePickerDialog: DatePickerDialogState = DatePickerDialogState(
        visible = false,
        selectedDate = DateUtils.currentDate,
        language = language
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
                config = config,
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
                initialSnapshot = InitialSnapshot(
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
    val amount: Double
)
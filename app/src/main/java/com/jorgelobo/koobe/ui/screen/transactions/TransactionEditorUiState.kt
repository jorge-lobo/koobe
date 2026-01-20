package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.utils.DateUtils
import java.util.Date

data class TransactionEditorUiState(
    val category: Category,
    val subcategory: Subcategory? = null,
    val shortcut: Shortcut? = null,
    val description: String? = "",
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
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType>
) {
    val hasUnsavedChanges: Boolean
        get() = category.id != initialSnapshot.category.id ||
                subcategory?.id != initialSnapshot.subcategory?.id ||
                shortcut?.id != initialSnapshot.shortcut?.id ||
                description != initialSnapshot.description ||
                !DateUtils.isSameDay(date, initialSnapshot.date) ||
                paymentMethodType != initialSnapshot.paymentMethodType ||
                currencyType != initialSnapshot.currencyType ||
                amount != initialSnapshot.amount

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
                    description = "",
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
                description = "",
                inputState = InputState.DEFAULT,
                initialSnapshot = InitialSnapshot(
                    category = category,
                    subcategory = subcategory,
                    shortcut = shortcut,
                    transactionType = config.transactionType,
                    description = "",
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

data class InitialSnapshot(
    val category: Category,
    val subcategory: Subcategory?,
    val shortcut: Shortcut?,
    val transactionType: TransactionType,
    val description: String?,
    val date: Date,
    val paymentMethodType: PaymentMethodType,
    val currencyType: CurrencyType,
    val amount: Double,
)
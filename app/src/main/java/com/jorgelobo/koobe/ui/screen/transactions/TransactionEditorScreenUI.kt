package com.jorgelobo.koobe.ui.screen.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.common.modifiers.clearFocusOnTap
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.transactions.components.TransactionAmountSection
import com.jorgelobo.koobe.ui.screen.transactions.components.TransactionCategorySection
import com.jorgelobo.koobe.ui.screen.transactions.components.TransactionDateSection
import com.jorgelobo.koobe.ui.screen.transactions.components.TransactionDescriptionSection
import com.jorgelobo.koobe.ui.screen.transactions.components.TransactionSaveButton
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.date.DateUtils

/**
 * Composable that renders the main UI of the Transaction Editor screen.
 *
 * It displays the following sections:
 * 1. Category summary with change button
 * 2. Transaction date with "Today" button and date picker
 * 3. Description input with reset functionality
 * 4. Amount editor with payment method, currency selector, and keypad
 * 5. Save button
 *
 * Each section is modularized into private composables for clarity.
 *
 * @param state The current UI state of the transaction editor.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param onChangeClick Callback when the user taps the category/subcategory change button.
 * @param onTodayClick Callback when the "Today" button is clicked to set the current date.
 * @param onDatePickClick Callback when the user opens the date picker dialog.
 * @param onDescriptionChange Callback when the description text is modified.
 * @param onResetDescriptionClick Callback when the description reset button is clicked.
 * @param onResetAmountClick Callback when the amount reset button is clicked.
 * @param onPaymentSelectorClick Callback when the payment method selector is opened.
 * @param onCurrencySelectorClick Callback when the currency selector is opened.
 * @param onKeyClick Callback when a keypad key is pressed to input the transaction amount.
 * @param onSaveClick Callback when the user taps the Save button.
 */
@Composable
fun TransactionEditorScreenUI(
    state: TransactionEditorUiState,
    modifier: Modifier = Modifier,
    onChangeClick: () -> Unit,
    onTodayClick: () -> Unit,
    onDatePickClick: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onResetDescriptionClick: () -> Unit,
    onResetAmountClick: () -> Unit,
    onPaymentSelectorClick: () -> Unit,
    onCurrencySelectorClick: () -> Unit,
    onKeyClick: (KeypadKey) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Spacing.Small))

        TransactionCategorySection(
            state = state,
            onChangeClick = onChangeClick
        )

        Spacer(modifier = Modifier.height(Spacing.Large))

        TransactionDateSection(
            state = state,
            onTodayClick = onTodayClick,
            onDatePickClick = onDatePickClick
        )

        Spacer(modifier = Modifier.weight(1f))

        TransactionDescriptionSection(
            state = state,
            onDescriptionChange = onDescriptionChange,
            onResetDescriptionClick = onResetDescriptionClick
        )

        Spacer(modifier = Modifier.height(Spacing.Large))

        TransactionAmountSection(
            state = state,
            onResetAmountClick = onResetAmountClick,
            onPaymentSelectorClick = onPaymentSelectorClick,
            onCurrencySelectorClick = onCurrencySelectorClick,
            onKeyClick = onKeyClick
        )

        Spacer(modifier = Modifier.height(Spacing.Large))

        TransactionSaveButton(
            state = state,
            onSaveClick = onSaveClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewTransactionEditorScreen() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        val category = Category(
            8,
            "Entertainment",
            IconPack.ENTERTAINMENT,
            "#FFD54F",
            TransactionType.EXPENSE
        )

        val subcategory = Subcategory(
            id = 5,
            categoryId = 8,
            name = "Movies",
            icon = IconPack.CINEMA
        )

        val shortcut = Shortcut(
            id = 2,
            name = "Football",
            icon = IconPack.SPORTS,
            categoryId = 8,
            transactionType = TransactionType.EXPENSE,
            paymentMethod = PaymentMethodType.CASH,
            currency = CurrencyType.EUR,
            amount = 15.00,
            repeat = false,
            period = null
        )

        TransactionEditorScreenUI(
            state = TransactionEditorUiState(
                category = category,
                subcategory = subcategory,
                shortcut = shortcut,
                descriptionSource = DescriptionSource.TextDescription("Ticket"),
                inputState = InputState.DEFAULT,
                date = DateUtils.currentDate,
                language = AppLanguage.ENGLISH,
                paymentMethodType = PaymentMethodType.CASH,
                currencyType = CurrencyType.EUR,
                amount = 0.0,
                isSaveButtonEnabled = false,
                isLoading = false,
                errorMessage = null,
                initialSnapshot = InitialSnapshot(
                    category = category,
                    subcategory = subcategory,
                    shortcut = shortcut,
                    transactionType = TransactionType.EXPENSE,
                    descriptionSource = DescriptionSource.Empty,
                    date = DateUtils.currentDate,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR,
                    amount = 0.0
                ),
                showSnackBar = false,
                discardDialog = ConfirmationDialogState(visible = false),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                ),
            ),
            onChangeClick = {},
            onTodayClick = {},
            onDatePickClick = {},
            onDescriptionChange = {},
            onResetDescriptionClick = {},
            onResetAmountClick = {},
            onPaymentSelectorClick = {},
            onCurrencySelectorClick = {},
            onKeyClick = {},
            onSaveClick = {}
        )
    }
}
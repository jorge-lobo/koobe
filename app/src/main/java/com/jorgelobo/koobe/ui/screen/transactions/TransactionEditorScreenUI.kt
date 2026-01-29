package com.jorgelobo.koobe.ui.screen.transactions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.inputs.fields.AppInputText
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputDate
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputDateConfig
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputFieldConfig
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.composed.amount.AmountEditor
import com.jorgelobo.koobe.ui.components.composed.amount.AmountEditorConfig
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummary
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummaryConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.asText
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.mappers.toIcon
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils
import com.jorgelobo.koobe.utils.resolvedColor

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
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
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

@Composable
private fun TransactionCategorySection(
    state: TransactionEditorUiState,
    onChangeClick: () -> Unit
) {
    CategorySummary(
        config = CategorySummaryConfig(
            icon = state.category.icon,
            color = state.category.resolvedColor(),
            categoryName = state.category.localizedName(),
            subcategoryName = state.subcategory?.localizedName() ?: state.shortcut?.name,
            onChangeClick = onChangeClick
        )
    )
}

@Composable
private fun TransactionDateSection(
    state: TransactionEditorUiState,
    onTodayClick: () -> Unit,
    onDatePickClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_today),
                type = ButtonType.SECONDARY_COMPACT,
                state = if (DateUtils.isSameDay(
                        state.date,
                        DateUtils.currentDate
                    )
                ) UiState.DISABLED else UiState.ENABLED,
                onClick = onTodayClick
            )
        )

        InputDate(
            config = InputDateConfig(
                date = state.date,
                onIconClick = onDatePickClick
            )
        )
    }
}

@Composable
private fun TransactionDescriptionSection(
    state: TransactionEditorUiState,
    onDescriptionChange: (String) -> Unit,
    onResetDescriptionClick: () -> Unit
) {
    AppInputText(
        config = InputFieldConfig(
            value = state.descriptionSource.asText(),
            label = stringResource(R.string.label_description),
            placeholder = stringResource(R.string.input_hint_description),
            state = state.inputState,
            onValueChange = onDescriptionChange,
            onResetClick = onResetDescriptionClick
        )
    )
}

@Composable
private fun TransactionAmountSection(
    state: TransactionEditorUiState,
    onResetAmountClick: () -> Unit,
    onPaymentSelectorClick: () -> Unit,
    onCurrencySelectorClick: () -> Unit,
    onKeyClick: (KeypadKey) -> Unit
) {
    AmountEditor(
        config = AmountEditorConfig(
            paymentIcon = state.paymentMethodType.toIcon(),
            currencyType = state.currencyType,
            value = state.amount,
            onResetClick = onResetAmountClick,
            onPaymentSelectorClick = onPaymentSelectorClick,
            onCurrencySelectorClick = onCurrencySelectorClick,
            onKeyClick = onKeyClick
        )
    )
}

@Composable
private fun TransactionSaveButton(
    state: TransactionEditorUiState,
    onSaveClick: () -> Unit
) {
    AppButton(
        ButtonConfig(
            text = stringResource(R.string.btn_save),
            type = ButtonType.PRIMARY,
            state = if (state.isSaveButtonEnabled) UiState.ENABLED else UiState.DISABLED,
            onClick = onSaveClick
        )
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewTransactionEditorScreen() {
    KoobeTheme {
        val category = Category(
            8,
            "Entertainment",
            IconPack.ENTERTAINMENT.icon,
            "#FFD54F",
            TransactionType.EXPENSE
        )

        val subcategory = Subcategory(
            id = 5,
            categoryId = 8,
            name = "Movies",
            icon = IconPack.CINEMA.icon
        )

        val shortcut = Shortcut(
            id = 2,
            name = "Football",
            icon = IconPack.SPORTS.icon,
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
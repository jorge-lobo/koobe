package com.jorgelobo.koobe.ui.screen.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.dialogs.AppDatePickerDialog
import com.jorgelobo.koobe.ui.components.base.dialogs.AppDatePickerDialogConfig
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialogConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheet
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheetConfig
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorScreen(
    navController: NavController,
    config: TransactionEditorConfig,
    viewModel: TransactionEditorViewModel = hiltViewModel()
) {
    BackHandler {
        viewModel.onDialogAction(ConfirmationDialogAction.RequestClose)
    }

    LaunchedEffect(config) {
        viewModel.init(config)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TransactionEditorEvent.ExitToOrigin -> {
                    navController.navigate(config.originRoute) {
                        popUpTo(config.originRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    if (uiState.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { viewModel.onDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { viewModel.onDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (uiState.currencyDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.CURRENCY,
                title = stringResource(R.string.dialog_headline_currency_selector),
                selectedCurrency = uiState.currencyDialog.selected
                    ?: uiState.currencyDialog.initial,
                onConfirm = { viewModel.onCurrencySelectorDialogAction(SelectorDialogAction.Apply) },
                onCancel = { viewModel.onCurrencySelectorDialogAction(SelectorDialogAction.Cancel) },
                onCurrencySelected = {
                    viewModel.onCurrencySelectorDialogAction(
                        SelectorDialogAction.Select(
                            it
                        )
                    )
                }
            )
        )
    }

    if (uiState.datePickerDialog.visible) {
        AppDatePickerDialog(
            config = AppDatePickerDialogConfig(
                visible = uiState.datePickerDialog.visible,
                selectedDate = uiState.datePickerDialog.selectedDate,
                onDateSelected = {
                    viewModel.onDatePickerDialogAction(
                        DatePickerDialogAction.Select(it)
                    )
                },
                onConfirm = {
                    viewModel.onDatePickerDialogAction(
                        DatePickerDialogAction.Confirm
                    )
                },
                onDismiss = {
                    viewModel.onDatePickerDialogAction(
                        DatePickerDialogAction.Dismiss
                    )
                }
            )
        )
    }

    if (uiState.paymentMethodSelector.visible) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.onPaymentSelectorAction(
                    SelectorSheetAction.Dismiss
                )
            }
        ) {
            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Payment(
                    selected = uiState.paymentMethodSelector.selected,
                    onItemSelected = {
                        viewModel.onPaymentSelectorAction(
                            SelectorSheetAction.Select(
                                it
                            )
                        )
                    }
                ),
                onDismiss = { viewModel.onPaymentSelectorAction(SelectorSheetAction.Dismiss) }
            )
        }
    }

    TransactionEditorScreenUI(
        config = config,
        state = uiState,
        onCloseClick = { viewModel.onDialogAction(ConfirmationDialogAction.RequestClose) },
        onDeleteClick = {},
        onChangeClick = {
            navController.navigate(
                Route.CategorySelector.create(
                    CategorySelectorConfig(
                        mode = CategorySelectorMode.EDIT_TRANSACTION,
                        target = CategorySelectorTarget.TRANSACTION_EDITOR,
                        initialTransactionType = config.transactionType
                    )
                )
            )
        },
        onTodayClick = { viewModel.onTodayClick() },
        onDatePickClick = {
            viewModel.onDatePickerDialogAction(
                DatePickerDialogAction.Open
            )
        },
        onDescriptionChange = { viewModel.onDescriptionChanged(it) },
        onResetDescriptionClick = { viewModel.onResetDescription() },
        onResetAmountClick = { viewModel.onResetAmount() },
        onPaymentSelectorClick = {
            viewModel.onPaymentSelectorAction(
                SelectorSheetAction.Open
            )
        },
        onCurrencySelectorClick = {
            viewModel.onCurrencySelectorDialogAction(
                SelectorDialogAction.Open
            )
        },
        onKeyClick = { viewModel.onKeyClicked(it) },
        onSaveClick = {}
    )
}
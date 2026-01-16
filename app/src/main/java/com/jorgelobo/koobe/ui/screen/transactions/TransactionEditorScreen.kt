package com.jorgelobo.koobe.ui.screen.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.UiEvent
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialogConfig
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.R

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

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                UiEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        onTodayClick = {},
        onDatePickClick = {},
        onDescriptionChange = { viewModel.onDescriptionChanged(it) },
        onResetDescriptionClick = { viewModel.onResetDescription() },
        onResetAmountClick = { viewModel.onResetAmount() },
        onPaymentSelectorClick = {},
        onCurrencySelectorClick = { viewModel.onCurrencySelectorDialogAction(SelectorDialogAction.Open) },
        onKeyClick = { viewModel.onKeyClicked(it) },
        onSaveClick = {}
    )
}
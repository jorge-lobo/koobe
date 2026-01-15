package com.jorgelobo.koobe.ui.screen.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.UiEvent
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

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
        onCurrencySelectorClick = {},
        onKeyClick = { viewModel.onKeyClicked(it) },
        onSaveClick = {}
    )
}
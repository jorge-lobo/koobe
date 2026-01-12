package com.jorgelobo.koobe.ui.screen.transactions

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

@Composable
fun TransactionEditorScreen(
    navController: NavController,
    config: TransactionEditorConfig,
    viewModel: TransactionEditorViewModel = hiltViewModel()
) {

    LaunchedEffect(config) {
        viewModel.init(config)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TransactionEditorScreenUI(
        config = config,
        state = uiState,
        onCloseClick = {},
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
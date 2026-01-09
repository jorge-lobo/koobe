package com.jorgelobo.koobe.ui.screen.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

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
        onChangeClick = {},
        onTodayClick = {},
        onDatePickClick = {},
        onDescriptionChange = {},
        onResetClick = {},
        onPaymentSelectorClick = {},
        onCurrencySelectorClick = {},
        onKeyClick = {},
        onSaveClick = {}
    )
}
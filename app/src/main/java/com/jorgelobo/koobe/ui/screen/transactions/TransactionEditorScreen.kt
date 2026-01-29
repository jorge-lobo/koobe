package com.jorgelobo.koobe.ui.screen.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.AppSnackBar
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorScreen(
    navController: NavController,
    config: TransactionEditorConfig,
    viewModel: TransactionEditorViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentSnackBarConfig by remember { mutableStateOf<SnackBarConfig?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val autoFillDescriptionState =
        rememberUpdatedState(uiState.subcategory?.localizedName() ?: uiState.shortcut?.name)

    BackHandler {
        viewModel.onDialogAction(ConfirmationDialogAction.RequestClose)
    }

    TransactionEditorEffects(
        navController = navController,
        config = config,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        scope = scope,
        autoFillDescriptionState = autoFillDescriptionState,
        onSnackBarConfigChange = { currentSnackBarConfig = it }
    )

    TransactionEditorDialogs(
        state = uiState,
        sheetState = sheetState,
        onDialogAction = { viewModel.onDialogAction(it) },
        onCurrencySelectorDialogAction = { viewModel.onCurrencySelectorDialogAction(it) },
        onDatePickerDialogAction = { viewModel.onDatePickerDialogAction(it) },
        onPaymentSelectorAction = { viewModel.onPaymentSelectorAction(it) }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                currentSnackBarConfig?.let { config ->
                    Box(modifier = Modifier.padding(Spacing.Medium)) {
                        AppSnackBar(
                            config = config,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(
                        uiState.headlineRes(
                            config.isEditMode,
                            config.transactionType
                        )
                    ),
                    leadingAction = AppBarAction(
                        icon = IconGeneral.CLOSE,
                        onClick = { viewModel.onDialogAction(ConfirmationDialogAction.RequestClose) }
                    ),
                    trailingActions = if (config.isEditMode) listOf(
                        AppBarAction(IconGeneral.DELETE, onClick = {})
                    ) else emptyList()
                )
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        TransactionEditorScreenUI(
            state = uiState,
            modifier = Modifier.padding(padding),
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
            onDatePickClick = { viewModel.onDatePickerDialogAction(DatePickerDialogAction.Open) },
            onDescriptionChange = { viewModel.onDescriptionChanged(it) },
            onResetDescriptionClick = { viewModel.onResetDescription() },
            onResetAmountClick = { viewModel.onResetAmount() },
            onPaymentSelectorClick = { viewModel.onPaymentSelectorAction(SelectorSheetAction.Open) },
            onCurrencySelectorClick = {
                viewModel.onCurrencySelectorDialogAction(SelectorDialogAction.Open)
            },
            onKeyClick = { viewModel.onKeyClicked(it) },
            onSaveClick = { viewModel.onSaveClick() }
        )
    }
}
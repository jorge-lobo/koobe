package com.jorgelobo.koobe.ui.screen.transactions

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TransactionEditorEffects(
    navController: NavController,
    config: TransactionEditorConfig,
    viewModel: TransactionEditorViewModel,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    autoFillDescriptionState: State<String?>,
    onSnackBarConfigChange: (SnackBarConfig?) -> Unit
) {
    LaunchedEffect(config) {
        viewModel.init(config)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is TransactionEditorEvent.ExitToOrigin -> {
                    navController.navigate(config.originRoute) {
                        popUpTo(config.originRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                is TransactionEditorEvent.ShowSnackBar -> {
                    onSnackBarConfigChange(
                        SnackBarConfig(
                            messageRes = event.messageRes,
                            actionLabelRes = event.actionLabelRes,
                            icon = event.icon,
                            onActionClick = {
                                autoFillDescriptionState.value?.let {
                                    viewModel.onSnackBarActionClick(it)
                                }
                                scope.launch {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                    onSnackBarConfigChange(null)
                                }
                            },
                            onIconClick = {
                                scope.launch {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                    onSnackBarConfigChange(null)
                                }
                            }
                        )
                    )

                    snackBarHostState.showSnackbar(
                        message = "",
                        actionLabel = null,
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }
    }
}
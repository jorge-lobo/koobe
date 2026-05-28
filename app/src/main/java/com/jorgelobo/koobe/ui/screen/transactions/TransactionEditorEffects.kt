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

/**
 * Collects and reacts to one-off side effects emitted by [TransactionEditorViewModel].
 *
 * This composable handles navigation events and snackbar presentations, ensuring they are
 * executed outside of the normal UI state flow.
 *
 * @property navController Navigation controller used to perform screen navigation actions.
 * @property config Editor configuration containing navigation origin and mode information.
 * @property viewModel ViewModel emitting editor events.
 * @property snackBarHostState Host state used to display snackbars.
 * @property scope Coroutine scope used for snackbar and UI side-effect operations.
 * @property autoFillDescriptionState Holds an optional description used for autofill actions.
 * @property onSnackBarConfigChange Callback used to update snackbar configuration state.
 */
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

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is TransactionEditorEvent.ExitToOrigin -> {
                    navController.navigate(config.originRoute) {
                        popUpTo(config.originRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                is TransactionEditorEvent.NavigateTo -> navController.navigate(event.route)

                is TransactionEditorEvent.ShowSnackBar -> {
                    onSnackBarConfigChange(
                        SnackBarConfig(
                            messageRes = event.messageRes,
                            actionLabelRes = event.actionLabelRes,
                            icon = event.icon,
                            onActionClick = {
                                autoFillDescriptionState.value?.let {
                                    viewModel.onIntent(TransactionEditorIntent.State.DescriptionInputChanged(it))
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
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
 * Composable that observes side effects emitted by [TransactionEditorViewModel] and triggers
 * corresponding UI actions such as navigation or snackBars.
 *
 * This function does not display UI itself, but instead reacts to one-off events:
 * - Navigates back to the origin screen when [TransactionEditorEvent.ExitToOrigin] is emitted
 * - Shows a snackBar when [TransactionEditorEvent.ShowSnackBar] is emitted, handling
 *   action and icon clicks to optionally auto-fill the transaction description.
 *
 * @param navController Controller used for navigation between screens.
 * @param config The transaction editor configuration.
 * @param viewModel The [TransactionEditorViewModel] providing state and events.
 * @param snackBarHostState The host state to display snackBars.
 * @param scope CoroutineScope used for launching snackBar dismissal or other side effects.
 * @param autoFillDescriptionState A state holding the text to auto-fill when the snackBar action is clicked.
 * @param onSnackBarConfigChange Callback triggered whenever the snackBar configuration changes.
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
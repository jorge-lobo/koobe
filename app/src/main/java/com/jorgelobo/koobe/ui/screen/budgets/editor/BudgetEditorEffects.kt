package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Handles the side effects for the Budget Editor screen, such as navigation and displaying
 * snackbar notifications based on events emitted by the [BudgetEditorViewModel].
 *
 * @param navController The controller used to handle screen navigation.
 * @param viewModel The view model that provides the stream of [BudgetEditorEvent]s.
 * @param snackBarHostState The state for managing the display of snackbars.
 * @param scope The coroutine scope used for launching snackbar-related tasks.
 * @param onSnackBarConfigChange Callback triggered to update the configuration of the custom snackbar UI.
 */
@Composable
fun BudgetEditorEffects(
    navController: NavController,
    viewModel: BudgetEditorViewModel,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onSnackBarConfigChange: (SnackBarConfig?) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BudgetEditorEvent.ExitToOrigin -> navController.popBackStack()

                is BudgetEditorEvent.NavigateTo -> navController.navigate(event.route)

                is BudgetEditorEvent.ShowSnackBar -> {
                    onSnackBarConfigChange(
                        SnackBarConfig(
                            messageRes = event.messageRes,
                            actionLabelRes = event.actionLabelRes,
                            icon = event.icon,
                            onActionClick = {
                                snackBarHostState.currentSnackbarData?.dismiss()
                                onSnackBarConfigChange(null)
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
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}
package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Handle one-off UI effects (navigation and snackBars) from [CategoryEditorViewModel].
 */
@Composable
fun CategoryEditorEffects(
    navController: NavController,
    viewModel: CategoryEditorViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onSnackBarConfigChange: (SnackBarConfig?) -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                CategoryEditorEvent.NavigateBack -> navController.popBackStack()

                is CategoryEditorEvent.NavigateTo -> navController.navigate(event.route)

                is CategoryEditorEvent.ShowSnackbar -> {
                    onSnackBarConfigChange(
                        SnackBarConfig(
                            messageRes = event.messageRes,
                            actionLabelRes = event.actionLabelRes,
                            icon = event.icon,
                            onActionClick = {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    onSnackBarConfigChange(null)
                                }
                            },
                            onIconClick = {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    onSnackBarConfigChange(null)
                                }
                            }
                        )
                    )

                    snackbarHostState.showSnackbar(
                        message = "",
                        actionLabel = null,
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }
    }
}
package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import com.jorgelobo.koobe.ui.navigation.NavResultKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Handles side effects for the Shortcut Editor screen, including navigation, snackbar messages,
 * and monitoring for results returned from other screens via the navigation back stack.
 *
 * @param navController The navigation controller used for screen transitions.
 */
@Composable
fun ShortcutEditorEffects(
    navController: NavController,
    viewModel: ShortcutEditorViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onSnackBarConfigChange: (SnackBarConfig?) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ShortcutEditorEvent.NavigateBack -> navController.popBackStack()

                is ShortcutEditorEvent.NavigateTo -> navController.navigate(event.route)

                is ShortcutEditorEvent.ShowSnackbar -> {
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
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(navController) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

        savedStateHandle
            ?.getStateFlow<Int?>(NavResultKeys.SELECTED_CATEGORY_ID, null)
            ?.collect { categoryId ->
                if (categoryId != null) {
                    viewModel.onIntent(ShortcutEditorIntent.State.CategoryChanged(categoryId))
                    savedStateHandle.remove<Int>(NavResultKeys.SELECTED_CATEGORY_ID)
                }
            }
    }
}
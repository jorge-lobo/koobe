package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SubcategoryEditorEffects(
    navController: NavController,
    viewModel: SubcategoryEditorViewModel,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onSnackBarConfigChange: (SnackBarConfig?) -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                SubcategoryEditorEvent.NavigateBack -> navController.popBackStack()

                is SubcategoryEditorEvent.NavigateTo -> navController.navigate(event.route)

                is SubcategoryEditorEvent.ShowSnackBar -> {
                    onSnackBarConfigChange(
                        SnackBarConfig(
                            messageRes = event.messageRes,
                            actionLabelRes = event.actionLabelRes,
                            icon = event.icon,
                            onActionClick = {
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
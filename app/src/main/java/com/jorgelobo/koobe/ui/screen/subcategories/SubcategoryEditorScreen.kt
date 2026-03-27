package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun SubcategoryEditorScreen(
    navController: NavController,
    config: SubcategoryEditorConfig,
    viewModel: SubcategoryEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubcategoryEditorEffects(
        navController = navController,
        viewModel = viewModel
    )

}
package com.jorgelobo.koobe.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jorgelobo.koobe.ui.app.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: AppViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    val isInitializing by viewModel.isInitializing.collectAsState()

    LaunchedEffect(isInitializing) {
        if (!isInitializing) {
            delay(4000)
            onFinished()
        }
    }

    SplashScreenUI()
}
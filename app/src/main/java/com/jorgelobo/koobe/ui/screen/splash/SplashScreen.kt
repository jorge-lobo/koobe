package com.jorgelobo.koobe.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jorgelobo.koobe.ui.app.AppViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun SplashScreen(
    viewModel: AppViewModel,
    onFinished: () -> Unit
) {
    val isInitializing by viewModel.isInitializing.collectAsState()

    LaunchedEffect(isInitializing) {
        if (!isInitializing) {
            delay(4.seconds)
            onFinished()
        }
    }

    SplashScreenUI()
}
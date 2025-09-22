package com.jorgelobo.koobe.ui.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme

@Composable
fun Background(
    type: BackgroundType
) {
    val backgroundColor = when (type) {
        BackgroundType.SPLASH -> AppTheme.colors.backgroundColors.splashBackground
        BackgroundType.SCREEN -> AppTheme.colors.backgroundColors.screenBackground
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBackground() {
    KoobeTheme {
        Background(BackgroundType.SPLASH)
    }
}
package com.jorgelobo.koobe.ui.screen.splash

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.LogoSize
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI() {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        startAnimation = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3500,
            easing = LinearOutSlowInEasing
        ),
        label = "SplashLogoFade"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundColors.splashBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = null,
            modifier = Modifier
                .width(LogoSize.SplashLogoWidth)
                .alpha(alpha)
        )

        Image(
            painter = painterResource(id = R.drawable.splash_symbol),
            contentDescription = null,
            modifier = Modifier.width(LogoSize.SplashLogoWidth)
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSplashScreen() {
    KoobeTheme {
        SplashScreenUI()
    }
}
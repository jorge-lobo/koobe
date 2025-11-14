package com.jorgelobo.koobe.ui.components.base.progressBar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.ProgressBarSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppProgressBar(
    config: ProgressBarConfig,
    modifier: Modifier = Modifier
) {
    val shapes = AppTheme.shapes
    val colors = AppTheme.colors.progressBarColors
    val progress = config.progress.coerceIn(0f, 1f)
    val projection = config.projection.coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
    )

    val animatedProjection by animateFloatAsState(
        targetValue = projection,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ProgressBarSize.Track)
            .clip(shape = shapes.small)
            .background(colors.progressBarTrack)
            .border(BorderDimens.Thin, colors.progressBarOutline)
    ) {
        // Projection
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProjection)
                .fillMaxHeight()
                .padding(Spacing.Mini)
                .clip(shape = shapes.extraSmall)
                .background(if (animatedProjection < 0.999f) AccentMint else AccentCoral)
                .animateContentSize()
        )

        // Actual progress
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .padding(Spacing.Mini)
                .clip(shape = shapes.extraSmall)
                .background(colors.progressBarActiveIndicator)
                .animateContentSize()
        )

        // Label
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = Spacing.ExtraSmall),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = config.percentageLabel,
                style = AppTheme.typography.numbers.labelSmall,
                color = colors.progressBarText
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewRadioButtons() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.containerColors.containerPrimary)
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AppProgressBar(
                config = ProgressBarConfig(
                    progress = 0.5f,
                    projection = 0.78f
                )
            )

            AppProgressBar(
                config = ProgressBarConfig(
                    progress = 0.65f,
                    projection = 1.23f
                )
            )
        }
    }
}
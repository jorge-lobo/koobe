package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.PeriodItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun PeriodItem(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    isFuture: Boolean = false,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.smallMedium

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetColor = when {
        isPressed -> colors.containerColors.containerSelected
        isSelected -> colors.containerColors.containerSelected
        isFuture -> colors.containerColors.containerDisabled
        else -> colors.containerColors.containerPrimary
    }

    val backgroundColor by animateColorAsState(targetColor, label = "")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(PeriodItemSize.Height)
            .background(backgroundColor, shape)
            .clickable(
                enabled = !isFuture,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = typography.titleSmall,
            color = if (isFuture) colors.textColors.textDisabled else colors.textColors.textSecondary
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewPeriodListItem() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            PeriodItem(
                label = "2025",
                isSelected = false,
                onClick = {}
            )

            PeriodItem(
                label = "2025",
                isSelected = true,
                onClick = {}
            )

            PeriodItem(
                label = "2025",
                isSelected = false,
                isFuture = true,
                onClick = {}
            )
        }
    }
}
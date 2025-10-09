package com.jorgelobo.koobe.ui.components.base.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.InputSelectorConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.SelectorSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun InputSelector(
    config: InputSelectorConfig,
    modifier: Modifier = Modifier
) {
    val shapes = AppTheme.shapes
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val icon = IconGeneral.DISCLOSURE.icon

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val iconColor by animateColorAsState(
        targetValue = if (isPressed) colors.iconColors.iconTextButton
        else colors.iconColors.iconPrimary,
        label = "iconColorAnimation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        label = "iconScale"
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = config.label,
            style = typography.bodySmall,
            color = colors.textColors.textLabel,
            modifier = Modifier
                .padding(start = Spacing.Medium, bottom = Spacing.Micro)
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(SelectorSize.InputSelectorHeight)
                .clip(shapes.medium)
                .border(1.dp, colors.containerColors.containerOutline, shapes.medium)
                .background(colors.containerColors.containerPrimary)
                .padding(start = Spacing.Medium),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = config.value,
                    style = typography.bodyLarge,
                    color = colors.textColors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = config.onClick,
                    interactionSource = interactionSource
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier
                            .size(IconSize.Medium)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewInputSelector() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            InputSelector(
                config = InputSelectorConfig(
                    onClick = {},
                    value = "English",
                    label = stringResource(R.string.label_language)
                )
            )
        }
    }
}
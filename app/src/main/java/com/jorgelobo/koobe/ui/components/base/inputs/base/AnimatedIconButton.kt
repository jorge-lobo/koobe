package com.jorgelobo.koobe.ui.components.base.inputs.base

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize

@Composable
fun AnimatedIconButton(
    modifier: Modifier = Modifier,
    icon: IconPack,
    contentDescription: String? = null,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val colors = AppTheme.colors
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

    IconButton(
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier
            .size(IconSize.Large)
            .graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        Icon(
            imageVector = icon.icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(IconSize.ExtraSmall)
        )
    }
}
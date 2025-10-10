package com.jorgelobo.koobe.ui.components.base.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseFieldContainer(
    modifier: Modifier = Modifier,
    label: String?,
    width: Dp? = null,
    height: Dp,
    content: @Composable RowScope.() -> Unit
) {
    val shape = AppTheme.shapes.medium
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    Column(modifier = modifier.then(if (width != null) Modifier.width(width) else Modifier)) {
        label?.let {
            Text(
                text = it,
                style = typography.bodySmall,
                color = colors.textColors.textLabel,
                modifier = Modifier.padding(start = Spacing.Medium, bottom = Spacing.Micro)
            )
        }

        Box(
            modifier = Modifier
                .height(height)
                .clip(shape)
                .border(BorderDimens.Base, colors.containerColors.containerOutline, shape)
                .background(colors.containerColors.containerPrimary)
                .padding(start = Spacing.Medium),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}
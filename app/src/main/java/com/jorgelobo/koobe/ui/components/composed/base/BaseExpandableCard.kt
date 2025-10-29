package com.jorgelobo.koobe.ui.components.composed.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonExpandCollapse
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseExpandableCard(
    modifier: Modifier = Modifier,
    headerContent: @Composable RowScope.() -> Unit,
    expandedContent: @Composable (ColumnScope.() -> Unit)? = null,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    headerPadding: Dp = Spacing.Small
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.medium

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = ListItemSize.MainHeight)
            .background(colors.containerColors.containerPrimary, shape)
            .border(BorderDimens.Base, colors.containerColors.containerOutline, shape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(headerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            headerContent()

            ButtonExpandCollapse(
                onClick = { onExpandedChange(!isExpanded) },
                isExpanded = isExpanded
            )
        }

        expandedContent?.let {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(250)) + fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing.Small),
                        verticalArrangement = Arrangement.SpaceBetween,
                        content = it
                    )
                }
            }
        }
    }
}
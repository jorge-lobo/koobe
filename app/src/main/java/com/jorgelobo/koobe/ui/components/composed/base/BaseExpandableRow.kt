package com.jorgelobo.koobe.ui.components.composed.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonExpandCollapse
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseExpandableRow(
    modifier: Modifier = Modifier,
    headerContent: @Composable RowScope.() -> Unit,
    expandedContent: @Composable (ColumnScope.() -> Unit)? = null,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ListItemSize.MainHeight)
                .padding(start = Spacing.Medium, top = Spacing.Tiny, end = Spacing.Small),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.backgroundColors.screenBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Spacing.Medium, end = Spacing.Small, top = Spacing.Small),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny),
                        content = it
                    )
                }
            }
        }
    }
}
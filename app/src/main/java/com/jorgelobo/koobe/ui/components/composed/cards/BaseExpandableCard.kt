package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonExpandCollapse
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseExpandableCard(
    modifier: Modifier = Modifier,
    headerContent: @Composable RowScope.() -> Unit,
    expandedContent: @Composable (ColumnScope.() -> Unit)? = null,
    isInitiallyExpanded: Boolean = false
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.medium
    var isExpanded by remember { mutableStateOf(isInitiallyExpanded) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = ListItemSize.MainHeight)
            .background(colors.containerColors.containerPrimary, shape)
            .padding(Spacing.Small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            headerContent()

            ButtonExpandCollapse(
                onClick = { isExpanded = !isExpanded },
                isExpanded = isExpanded
            )
        }

        expandedContent?.let {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(250)) + fadeOut()
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
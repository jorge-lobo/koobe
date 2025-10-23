package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.PeriodGridContainerSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils

@Composable
fun MonthGrid(
    modifier: Modifier = Modifier,
    config: SelectableListConfig
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.medium
    val backgroundColor = colors.containerColors.containerSecondary
    val chunkedMonths = config.items.chunked(4)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(PeriodGridContainerSize.Height)
            .background(backgroundColor, shape)
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Micro),
            verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
        ) {
            chunkedMonths.forEachIndexed { rowIndex, rowItems ->
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Micro)
                ) {
                    rowItems.forEachIndexed { colIndex, label ->
                        val index = rowIndex * 4 + colIndex
                        PeriodItem(
                            label = label,
                            isSelected = index == config.selectedIndex,
                            onClick = { config.onItemSelected(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewMonthGrid() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var selectedIndex by remember { mutableIntStateOf(9) }
            val months = (0..11).map { DateUtils.getMonthShortName(it) }

            MonthGrid(
                config = SelectableListConfig(
                    items = months,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it }
                )
            )
        }
    }
}
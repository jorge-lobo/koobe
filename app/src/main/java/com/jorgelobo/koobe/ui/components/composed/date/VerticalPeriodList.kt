package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.PeriodListContainerSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import kotlinx.coroutines.flow.collectLatest

@Composable
fun VerticalPeriodList(
    modifier: Modifier = Modifier,
    config: SelectableListConfig,
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.medium
    val scrollState = rememberScrollState()

    LaunchedEffect(config.items.size) {
        snapshotFlow { scrollState.maxValue }.collectLatest {
            scrollState.scrollTo(it)
        }
    }

    Box(
        modifier = modifier
            .width(PeriodListContainerSize.Width)
            .height(PeriodListContainerSize.Height)
            .background(colors.containerColors.containerSecondary, shape)
            .clipToBounds()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Micro),
            verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
        ) {
            config.items.forEachIndexed { index, label ->
                PeriodItem(
                    label = label,
                    isSelected = index == config.selectedIndex,
                    onClick = { config.onItemSelected(index) },
                )
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewVerticalPeriodList() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var selectedIndex by remember { mutableIntStateOf(9) }
            val items = (2015..2025).map { it.toString() }

            VerticalPeriodList(
                config = SelectableListConfig(
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it }
                )
            )
        }
    }
}
package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.PeriodListContainerSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.date.DateFutureUtils
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

@Composable
fun VerticalPeriodList(
    modifier: Modifier = Modifier,
    config: SelectableListConfig,
    periodType: PeriodType,
    referenceDate: Date
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.medium
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex =
            (config.selectedIndex - 2).coerceAtLeast(0)
    )

    LaunchedEffect(config.selectedIndex) {
        listState.animateScrollToItem(
            index = (config.selectedIndex - 2).coerceAtLeast(0)
        )
    }

    Column(
        modifier = modifier
            .width(PeriodListContainerSize.Width)
            .heightIn(max = PeriodListContainerSize.Height)
            .background(colors.containerColors.containerSecondary, shape)
            .clipToBounds(),

        verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
    ) {
        LazyColumn(
            modifier = Modifier.padding(Spacing.Micro),
            verticalArrangement = Arrangement.spacedBy(Spacing.Micro),
            state = listState,
        ) {
            items(config.items.size) { index ->
                val isSelected = index == config.selectedIndex
                val startOfWeek = config.startOfWeek

                val isFuture = when (periodType) {
                    PeriodType.DAILY -> DateFutureUtils.isDayInFuture(index, referenceDate)
                    PeriodType.WEEKLY -> DateFutureUtils.isWeekInFuture(index, referenceDate, startOfWeek)
                    PeriodType.MONTHLY -> false
                    PeriodType.YEARLY -> false
                }

                PeriodItem(
                    label = config.items[index],
                    isSelected = isSelected,
                    isFuture = isFuture,
                    onClick = { if (!isFuture) config.onItemSelected(index) },
                )
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewVerticalPeriodList() {
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
            var selectedIndex by remember { mutableIntStateOf(9) }
            val items = (2015..2025).map { it.toString() }

            VerticalPeriodList(
                config = SelectableListConfig(
                    startOfWeek = StartOfWeek.SUNDAY,
                    items = items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it }
                ),
                periodType = PeriodType.YEARLY,
                referenceDate = DateUtils.currentDate
            )
        }
    }
}
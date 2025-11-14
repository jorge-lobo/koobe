package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.bottomSheet.BaseBottomSheet
import com.jorgelobo.koobe.ui.theme.dimens.BottomSheetSize
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.toggles.PeriodToggle
import com.jorgelobo.koobe.ui.components.base.toggles.periodToggleConfig
import com.jorgelobo.koobe.ui.components.composed.buttons.ConfirmCancelButtons
import com.jorgelobo.koobe.ui.components.composed.buttons.ConfirmCancelButtonsConfig
import com.jorgelobo.koobe.ui.components.composed.date.DateSelector
import com.jorgelobo.koobe.ui.components.composed.date.DateSelectorConfig
import com.jorgelobo.koobe.ui.components.composed.date.MonthGrid
import com.jorgelobo.koobe.ui.components.composed.date.SelectableListConfig
import com.jorgelobo.koobe.ui.components.composed.date.VerticalPeriodList
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils

@Composable
fun PeriodFilterBottomSheet(
    modifier: Modifier = Modifier,
    config: PeriodFilterBottomSheetConfig,
    onDismiss: () -> Unit
) {
    val months = remember { DateUtils.getAllMonthsShortNames() }
    var isEnabled by remember { mutableStateOf(false) }

    BaseBottomSheet(
        modifier = modifier,
        height = BottomSheetSize.Height.PeriodFilter,
        title = stringResource(R.string.bottom_sheet_headline_period_selector)
    ) {
        PeriodToggle(
            config = periodToggleConfig(
                selected = config.selectedType,
                onOptionSelected = { selected ->
                    config.onTypeSelected(selected)
                    isEnabled = true
                }
            )
        )

        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

        DateSelector(
            config = DateSelectorConfig(
                periodType = config.selectedType,
                date = config.date,
                onLeftClick = {
                    config.onLeftClick()
                    isEnabled = true
                },
                onRightClick = {
                    config.onRightClick()
                    isEnabled = true
                },
                onPickerClick = {
                    config.onPickerClick()
                    isEnabled = true
                }
            ),
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PeriodListContent(
                periodType = config.selectedType,
                months = months,
                config = config,
                onItemSelected = { isEnabled = true }
            )
        }

        Spacer(modifier = Modifier.height(Spacing.MediumLarge))

        ConfirmCancelButtons(
            config = ConfirmCancelButtonsConfig(
                confirmText = stringResource(R.string.btn_apply),
                cancelText = stringResource(R.string.btn_cancel),
                isConfirmEnabled = isEnabled,
                onConfirmClick = config.onConfirmClick,
                onCancelClick = onDismiss
            )
        )
    }
}

@Composable
private fun PeriodListContent(
    periodType: PeriodType,
    months: List<String>,
    config: PeriodFilterBottomSheetConfig,
    onItemSelected: () -> Unit
) {
    when (periodType) {
        PeriodType.DAILY -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = config.dailyItems,
                    selectedIndex = config.selectedDailyIndex,
                    onItemSelected = {
                        config.onDailySelected(it)
                        onItemSelected()
                    }
                )
            )
        }

        PeriodType.WEEKLY -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = config.weeklyItems,
                    selectedIndex = config.selectedWeeklyIndex,
                    onItemSelected = {
                        config.onWeeklySelected(it)
                        onItemSelected()
                    }
                )
            )
        }

        PeriodType.MONTHLY -> {
            MonthGrid(
                config = SelectableListConfig(
                    items = months,
                    selectedIndex = config.selectedMonthlyIndex,
                    onItemSelected = {
                        config.onMonthlySelected(it)
                        onItemSelected()
                    }
                )
            )
        }

        PeriodType.YEARLY -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = config.yearlyItems,
                    selectedIndex = config.selectedYearlyIndex,
                    onItemSelected = {
                        config.onYearlySelected(it)
                        onItemSelected()
                    }
                )
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewPeriodFilterBottomSheet() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var periodSelected by remember { mutableStateOf(PeriodType.MONTHLY) }
            var selectedDailyIndex by remember { mutableIntStateOf(4) }
            var selectedWeeklyIndex by remember { mutableIntStateOf(44) }
            var selectedMonthlyIndex by remember { mutableIntStateOf(10) }
            var selectedYearlyIndex by remember { mutableIntStateOf(10) }
            val dailyItems = (1..30).map { it.toString() }
            val weeklyItems = (1..52).map { it.toString() }
            val yearlyItems = (2015..2025).map { it.toString() }

            PeriodFilterBottomSheet(
                config = PeriodFilterBottomSheetConfig(
                    type = PeriodType.WEEKLY,
                    selectedType = periodSelected,
                    onTypeSelected = { periodSelected = it },
                    date = DateUtils.currentDate,
                    onLeftClick = {},
                    onRightClick = {},
                    onPickerClick = {},
                    dailyItems = dailyItems,
                    weeklyItems = weeklyItems,
                    yearlyItems = yearlyItems,
                    selectedDailyIndex = selectedDailyIndex,
                    selectedWeeklyIndex = selectedWeeklyIndex,
                    selectedMonthlyIndex = selectedMonthlyIndex,
                    selectedYearlyIndex = selectedYearlyIndex,
                    onDailySelected = { selectedDailyIndex = it },
                    onWeeklySelected = { selectedWeeklyIndex = it },
                    onMonthlySelected = { selectedMonthlyIndex = it },
                    onYearlySelected = { selectedYearlyIndex = it },
                    onConfirmClick = {}
                ),
                onDismiss = {}
            )
        }
    }
}
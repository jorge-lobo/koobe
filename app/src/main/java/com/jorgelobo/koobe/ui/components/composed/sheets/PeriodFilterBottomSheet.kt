package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.bottomSheet.AppModalBottomSheet
import com.jorgelobo.koobe.ui.components.base.bottomSheet.BaseBottomSheetContent
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
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodFilterBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    config: PeriodFilterBottomSheetConfig,
    onDismiss: () -> Unit
) {
    val months = remember { DateUtils.getAllMonthsShortNames() }
    var isEnabled by remember { mutableStateOf(false) }

    AppModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        BaseBottomSheetContent(
            modifier = modifier,
            title = stringResource(R.string.bottom_sheet_headline_period_selector),
            showHandle = true
        ) {
            PeriodToggle(
                config = periodToggleConfig(
                    selected = config.selected.type,
                    onOptionSelected = { selected ->
                        config.onSelectionChanged(
                            config.selected.copy(type = selected)
                        )
                        isEnabled = true
                    }
                )
            )

            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

            DateSelector(
                config = DateSelectorConfig(
                    periodType = config.selected.type,
                    date = config.selected.date,
                    onLeftClick = {
                        config.dateNavigation.onLeftClick()
                        isEnabled = true
                    },
                    onRightClick = {
                        config.dateNavigation.onRightClick()
                        isEnabled = true
                    },
                    onPickerClick = {
                        config.dateNavigation.onPickerClick()
                        isEnabled = true
                    }
                ),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PeriodListContent(
                    months = months,
                    periodConfig = config.periodConfig,
                    onItemSelected = { isEnabled = true },
                    referenceDate = config.selected.date
                )
            }

            Spacer(modifier = Modifier.height(Spacing.MediumLarge))

            ConfirmCancelButtons(
                config = ConfirmCancelButtonsConfig(
                    confirmText = stringResource(R.string.btn_apply),
                    cancelText = stringResource(R.string.btn_cancel),
                    isConfirmEnabled = isEnabled,
                    onConfirmClick = config.actions.onApply,
                    onCancelClick = onDismiss
                )
            )
        }
    }
}

@Composable
private fun PeriodListContent(
    months: List<String>,
    periodConfig: PeriodConfig,
    referenceDate: Date,
    onItemSelected: () -> Unit
) {
    when (periodConfig) {
        is PeriodConfig.Daily -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = periodConfig.items,
                    selectedIndex = periodConfig.selectedIndex,
                    onItemSelected = {
                        periodConfig.onItemSelected(it)
                        onItemSelected()
                    }
                ),
                periodType = PeriodType.DAILY,
                referenceDate = referenceDate
            )
        }

        is PeriodConfig.Weekly -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = periodConfig.items,
                    selectedIndex = periodConfig.selectedIndex,
                    onItemSelected = {
                        periodConfig.onItemSelected(it)
                        onItemSelected()
                    }
                ),
                periodType = PeriodType.WEEKLY,
                referenceDate = referenceDate
            )
        }

        is PeriodConfig.Monthly -> {
            MonthGrid(
                config = SelectableListConfig(
                    items = months,
                    selectedIndex = periodConfig.selectedIndex,
                    onItemSelected = {
                        periodConfig.onItemSelected(it)
                        onItemSelected()
                    }
                ),
                referenceDate = referenceDate
            )
        }

        is PeriodConfig.Yearly -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = periodConfig.items,
                    selectedIndex = periodConfig.selectedIndex,
                    onItemSelected = {
                        periodConfig.onItemSelected(it)
                        onItemSelected()
                    }
                ),
                periodType = PeriodType.YEARLY,
                referenceDate = referenceDate
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberPreviewSheetState(): SheetState =
    rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

@OptIn(ExperimentalMaterial3Api::class)
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewPeriodFilterBottomSheet() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        val sheetState = rememberPreviewSheetState()

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var currentSelection by remember {
                mutableStateOf(
                    PeriodSelection(
                        type = PeriodType.DAILY,
                        date = DateUtils.currentDate
                    )
                )
            }

            var selectedDailyIndex by remember { mutableIntStateOf(4) }
            var selectedWeeklyIndex by remember { mutableIntStateOf(44) }
            var selectedMonthlyIndex by remember { mutableIntStateOf(10) }
            var selectedYearlyIndex by remember { mutableIntStateOf(10) }

            val dailyItems = (1..30).map { it.toString() }
            val weeklyItems = (1..52).map { it.toString() }
            val yearlyItems = (2015..2025).map { it.toString() }

            PeriodFilterBottomSheet(
                sheetState = sheetState,
                config = PeriodFilterBottomSheetConfig(
                    selected = currentSelection,
                    onSelectionChanged = { selection ->
                        currentSelection = selection
                    },
                    dateNavigation = DateNavigation(
                        onLeftClick = {},
                        onRightClick = {},
                        onPickerClick = {}
                    ),
                    periodConfig = when (currentSelection.type) {
                        PeriodType.DAILY -> PeriodConfig.Daily(
                            items = dailyItems,
                            selectedIndex = selectedDailyIndex,
                            onItemSelected = { index -> selectedDailyIndex = index }
                        )

                        PeriodType.WEEKLY -> PeriodConfig.Weekly(
                            items = weeklyItems,
                            selectedIndex = selectedWeeklyIndex,
                            onItemSelected = { index -> selectedWeeklyIndex = index }
                        )

                        PeriodType.MONTHLY -> PeriodConfig.Monthly(
                            items = emptyList(),
                            selectedIndex = selectedMonthlyIndex,
                            onItemSelected = { index -> selectedMonthlyIndex = index }
                        )

                        PeriodType.YEARLY -> PeriodConfig.Yearly(
                            items = yearlyItems,
                            selectedIndex = selectedYearlyIndex,
                            onItemSelected = { index -> selectedYearlyIndex = index }
                        )
                    },
                    actions = FilterActions(
                        onApply = {},
                        onCancel = {}
                    )
                ),
                onDismiss = {}
            )
        }
    }
}
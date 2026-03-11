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
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
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
import com.jorgelobo.koobe.utils.date.DateUtils
import com.jorgelobo.koobe.utils.date.PeriodUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodFilterBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    config: PeriodFilterBottomSheetConfig,
    onDismiss: () -> Unit
) {
    remember { PeriodUtils.getAllMonthsShortNames() }
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
                    state = config.periodListState,
                    onItemSelected = {
                        config.onPeriodItemSelected(it)
                        isEnabled = true
                    }
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
    state: PeriodListState,
    onItemSelected: (Int) -> Unit
) {
    when (state.periodType) {
        PeriodType.MONTHLY -> {
            MonthGrid(
                config = SelectableListConfig(
                    items = state.items,
                    selectedIndex = state.selectedIndex,
                    onItemSelected = onItemSelected
                ),
                referenceDate = state.referenceDate
            )
        }

        else -> {
            VerticalPeriodList(
                config = SelectableListConfig(
                    items = state.items,
                    selectedIndex = state.selectedIndex,
                    onItemSelected = onItemSelected
                ),
                periodType = state.periodType,
                referenceDate = state.referenceDate
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

            var selectedYearlyIndex by remember { mutableIntStateOf(10) }

            val yearlyItems = (2015..2025).map { it.toString() }

            PeriodFilterBottomSheet(
                sheetState = sheetState,
                config = PeriodFilterBottomSheetConfig(
                    startOfWeek = StartOfWeek.SUNDAY,
                    selected = currentSelection,
                    onSelectionChanged = { selection ->
                        currentSelection = selection
                    },
                    dateNavigation = DateNavigation(
                        onLeftClick = {},
                        onRightClick = {},
                        onPickerClick = {}
                    ),
                    actions = FilterActions(
                        onOpenDatePicker = {},
                        onApply = {},
                        onCancel = {}
                    ),
                    periodListState = PeriodListState(
                        items = yearlyItems,
                        selectedIndex = selectedYearlyIndex,
                        periodType = PeriodType.YEARLY,
                        referenceDate = DateUtils.currentDate
                    ),
                    onPeriodItemSelected = { index -> selectedYearlyIndex = index }
                ),
                onDismiss = {}
            )
        }
    }
}
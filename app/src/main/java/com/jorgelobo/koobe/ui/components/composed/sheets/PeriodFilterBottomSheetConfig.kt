package com.jorgelobo.koobe.ui.components.composed.sheets

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import java.util.Date

data class PeriodFilterBottomSheetConfig(
    val startOfWeek: StartOfWeek,
    val selected: PeriodSelection,
    val onSelectionChanged: (PeriodSelection) -> Unit,
    val dateNavigation: DateNavigation,
    val periodListState: PeriodListState,
    val onPeriodItemSelected: (Int) -> Unit,
    val actions: FilterActions
)

data class PeriodSelection(
    val type: PeriodType,
    val date: Date
)

data class DateNavigation(
    val onLeftClick: () -> Unit,
    val onRightClick: () -> Unit,
    val onPickerClick: () -> Unit
)

data class FilterActions(
    val onOpenDatePicker: () -> Unit,
    val onApply: () -> Unit,
    val onCancel: () -> Unit
)

data class PeriodListState(
    val items: List<String>,
    val selectedIndex: Int,
    val periodType: PeriodType,
    val referenceDate: Date
)
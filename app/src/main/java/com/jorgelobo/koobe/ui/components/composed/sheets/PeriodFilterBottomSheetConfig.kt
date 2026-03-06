package com.jorgelobo.koobe.ui.components.composed.sheets

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import java.util.Date

data class PeriodFilterBottomSheetConfig(
    val selected: PeriodSelection,
    val onSelectionChanged: (PeriodSelection) -> Unit,
    val dateNavigation: DateNavigation,
    val periodConfig: PeriodConfig,
    val actions: FilterActions
)

data class PeriodSelection(
    val type: PeriodType,
    val date: Date,
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

sealed class PeriodConfig {
    data class Daily(
        val items: List<String>,
        val selectedIndex: Int,
        val onItemSelected: (Int) -> Unit
    ) : PeriodConfig()

    data class Weekly(
        val items: List<String>,
        val selectedIndex: Int,
        val onItemSelected: (Int) -> Unit
    ) : PeriodConfig()

    data class Monthly(
        val items: List<String>,
        val selectedIndex: Int,
        val onItemSelected: (Int) -> Unit
    ) : PeriodConfig()

    data class Yearly(
        val items: List<String>,
        val selectedIndex: Int,
        val onItemSelected: (Int) -> Unit
    ) : PeriodConfig()
}
package com.jorgelobo.koobe.ui.components.composed.sheets

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import java.util.Date

data class PeriodFilterBottomSheetConfig(
    val type: PeriodType,
    val selectedType: PeriodType,
    val onTypeSelected: (PeriodType) -> Unit,
    val date: Date,
    val selectedDate: Date,
    val onDateSelected: (Date) -> Unit,
    val onLeftClick: () -> Unit,
    val onRightClick: () -> Unit,
    val onPickerClick: () -> Unit,
    val dailyItems: List<String>,
    val weeklyItems: List<String>,
    val yearlyItems: List<String>,
    val selectedDailyIndex: Int,
    val selectedWeeklyIndex: Int,
    val selectedMonthlyIndex: Int,
    val selectedYearlyIndex: Int,
    val onDailySelected: (Int) -> Unit,
    val onWeeklySelected: (Int) -> Unit,
    val onMonthlySelected: (Int) -> Unit,
    val onYearlySelected: (Int) -> Unit,
    val onApply: () -> Unit,
    val onCancel: () -> Unit
)
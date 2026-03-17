package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

/**
 * Represents the UI state for the Historic screen, maintaining the data required to display
 * transaction history, financial summaries, and filter configurations.
 *
 * @property language The current language settings of the application.
 * @property currencyType The currency format used for displaying financial values.
 * @property startOfWeek The configured first day of the week for period calculations.
 * @property categories The list of categories and their associated data to be displayed in the history.
 * @property transactionTypeSelected The currently active transaction filter (e.g., Expense or Income).
 * @property date The reference date used to determine the visible historical period.
 * @property periodType The granularity of the history being viewed (e.g., Monthly, Weekly).
 * @property balance The net total for the selected period.
 */
data class HistoricUiState(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val startOfWeek: StartOfWeek = StartOfWeek.SUNDAY,
    val categories: List<CategoryHistoricUi> = emptyList(),
    val transactionTypeSelected: TransactionType = TransactionType.EXPENSE,
    val date: Date = DateUtils.currentDate,
    val periodType: PeriodType = PeriodType.MONTHLY,
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val periodFilter: PeriodFilterSheetState = PeriodFilterSheetState(),
    val datePickerDialog: DatePickerDialogState = DatePickerDialogState(
        visible = false,
        selectedDate = DateUtils.currentDate,
        language = language
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
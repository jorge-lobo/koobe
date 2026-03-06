package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.utils.DateUtils
import java.util.Date

data class HistoricUiState(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val currencyType: CurrencyType = CurrencyType.EUR,
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
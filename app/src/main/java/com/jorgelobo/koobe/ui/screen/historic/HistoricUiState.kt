package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.utils.DateUtils
import java.util.Date

data class HistoricUiState(
    val currencyType: CurrencyType = CurrencyType.EUR,
    val categories: List<CategoryHistoricUi> = emptyList(),
    val transactionTypeSelected: TransactionType = TransactionType.EXPENSE,
    val date: Date = DateUtils.currentDate,
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
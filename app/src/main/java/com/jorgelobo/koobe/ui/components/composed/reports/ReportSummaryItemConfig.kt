package com.jorgelobo.koobe.ui.components.composed.reports

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType

@Stable
data class ReportSummaryItemConfig(
    val label: String,
    val month: String? = null,
    val amount: Double,
    val currencyType: CurrencyType
)

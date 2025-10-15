package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class MetricType : UiLabel {
    BALANCE, EXPENSE, INCOME;

    override fun toLabel() = when (this) {
        BALANCE -> R.string.toggle_balance
        EXPENSE -> R.string.toggle_expenses
        INCOME -> R.string.toggle_income
    }
}
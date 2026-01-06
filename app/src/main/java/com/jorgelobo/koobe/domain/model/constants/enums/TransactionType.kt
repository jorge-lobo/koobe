package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType : UiLabel {
    EXPENSE, INCOME;

    override fun toLabel() = when (this) {
        EXPENSE -> R.string.toggle_expenses
        INCOME -> R.string.toggle_income
    }
}
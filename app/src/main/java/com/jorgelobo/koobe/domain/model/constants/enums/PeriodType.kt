package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class PeriodType : UiLabel {
    YEARLY, MONTHLY, WEEKLY, DAILY;

    override fun toLabel() = when (this) {
        DAILY -> R.string.toggle_day
        WEEKLY -> R.string.toggle_week
        MONTHLY -> R.string.toggle_month
        YEARLY -> R.string.toggle_year
    }

}
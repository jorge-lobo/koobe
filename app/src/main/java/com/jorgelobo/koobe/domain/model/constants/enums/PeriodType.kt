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

    fun toRecurrenceLabel() = when (this) {
        DAILY -> R.string.recurrence_daily
        WEEKLY -> R.string.recurrence_weekly
        MONTHLY -> R.string.recurrence_monthly
        YEARLY -> R.string.recurrence_yearly
    }
}
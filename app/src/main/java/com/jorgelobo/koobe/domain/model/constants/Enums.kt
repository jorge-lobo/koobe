package com.jorgelobo.koobe.domain.model.constants

import com.jorgelobo.koobe.R

enum class TransactionType : ToggleLabel {
    EXPENSE, INCOME;

    override fun toLabel() = when (this) {
        EXPENSE -> R.string.toggle_expenses
        INCOME -> R.string.toggle_income
    }
}

enum class MetricType : ToggleLabel {
    BALANCE, EXPENSE, INCOME;

    override fun toLabel() = when (this) {
        BALANCE -> R.string.toggle_balance
        EXPENSE -> R.string.toggle_expenses
        INCOME -> R.string.toggle_income
    }
}

enum class PeriodType : ToggleLabel {
    YEARLY, MONTHLY, WEEKLY, DAILY;
    override fun toLabel() = when (this) {
        DAILY -> R.string.toggle_day
        WEEKLY -> R.string.toggle_week
        MONTHLY -> R.string.toggle_month
        YEARLY -> R.string.toggle_year
    }

}

enum class ThemeOption : ToggleLabel {
    LIGHT, DARK, SYSTEM;
    override fun toLabel() = when (this) {
        LIGHT -> R.string.toggle_light
        DARK -> R.string.toggle_dark
        SYSTEM -> R.string.toggle_system
    }

}

enum class PaymentMethodType { CASH, CARD, TRANSFER, CRYPTO }
enum class CurrencyType { EUR, USD, GBP }
enum class StartOfWeek { SUNDAY, MONDAY }
enum class AppLanguage { ENGLISH, PORTUGUESE }
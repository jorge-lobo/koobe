package com.jorgelobo.koobe.domain.model.constants

import com.jorgelobo.koobe.R

enum class TransactionType : UiLabel {
    EXPENSE, INCOME;

    override fun toLabel() = when (this) {
        EXPENSE -> R.string.toggle_expenses
        INCOME -> R.string.toggle_income
    }
}

enum class MetricType : UiLabel {
    BALANCE, EXPENSE, INCOME;

    override fun toLabel() = when (this) {
        BALANCE -> R.string.toggle_balance
        EXPENSE -> R.string.toggle_expenses
        INCOME -> R.string.toggle_income
    }
}

enum class PeriodType : UiLabel {
    YEARLY, MONTHLY, WEEKLY, DAILY;

    override fun toLabel() = when (this) {
        DAILY -> R.string.toggle_day
        WEEKLY -> R.string.toggle_week
        MONTHLY -> R.string.toggle_month
        YEARLY -> R.string.toggle_year
    }

}

enum class ThemeOption : UiLabel {
    LIGHT, DARK, SYSTEM;

    override fun toLabel() = when (this) {
        LIGHT -> R.string.toggle_light
        DARK -> R.string.toggle_dark
        SYSTEM -> R.string.toggle_system
    }

}

enum class ReportsTabs : UiLabel {
    OVERVIEW, TRENDS, CATEGORIES, METHODS;

    override fun toLabel() = when (this) {
        OVERVIEW -> R.string.tab_overview
        TRENDS -> R.string.tab_categories
        CATEGORIES -> R.string.tab_categories
        METHODS -> R.string.tab_methods
    }
}

enum class PaymentMethodType : UiLabel {
    CASH, CARD, TRANSFER, CRYPTO;

    override fun toLabel() = when (this) {
        CASH -> R.string.radio_cash
        CARD -> R.string.radio_card
        TRANSFER -> R.string.radio_transfer
        CRYPTO -> R.string.radio_crypto
    }
}

enum class CurrencyType : UiLabel {
    EUR, USD, GBP;

    override fun toLabel() = when (this) {
        EUR -> R.string.radio_euro
        USD -> R.string.radio_dollar
        GBP -> R.string.radio_pound
    }
}

enum class StartOfWeek : UiLabel {
    SUNDAY, MONDAY;

    override fun toLabel() = when (this) {
        SUNDAY -> R.string.radio_sunday
        MONDAY -> R.string.radio_monday
    }
}

enum class AppLanguage : UiLabel {
    ENGLISH, PORTUGUESE;

    override fun toLabel() = when (this) {
        ENGLISH -> R.string.radio_english
        PORTUGUESE -> R.string.radio_portuguese
    }
}
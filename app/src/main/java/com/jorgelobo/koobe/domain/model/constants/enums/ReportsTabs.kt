package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class ReportsTabs : UiLabel {
    OVERVIEW, TRENDS, CATEGORIES, METHODS;

    override fun toLabel() = when (this) {
        OVERVIEW -> R.string.tab_overview
        TRENDS -> R.string.tab_categories
        CATEGORIES -> R.string.tab_categories
        METHODS -> R.string.tab_methods
    }
}
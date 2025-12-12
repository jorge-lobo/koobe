package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class CategoryDetailType : UiLabel {
    SUBCATEGORIES, SHORTCUTS;

    override fun toLabel() = when (this) {
        SUBCATEGORIES -> R.string.toggle_subcategories
        SHORTCUTS -> R.string.toggle_shortcuts
    }
}
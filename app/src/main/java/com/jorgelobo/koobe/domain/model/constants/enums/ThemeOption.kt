package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class ThemeOption : UiLabel {
    LIGHT, DARK, SYSTEM;

    override fun toLabel() = when (this) {
        LIGHT -> R.string.toggle_light
        DARK -> R.string.toggle_dark
        SYSTEM -> R.string.toggle_system
    }

}
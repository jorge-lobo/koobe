package com.jorgelobo.koobe.domain.model.settings

import com.jorgelobo.koobe.domain.model.constants.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.ThemeOption

data class UserSettings(
    val theme: ThemeOption,
    val language: AppLanguage,
    val currency: CurrencyType,
    val startOfWeek: StartOfWeek
)
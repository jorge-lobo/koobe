package com.jorgelobo.koobe.domain.model.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption

data class UserSettings(
    val theme: ThemeOption,
    val language: AppLanguage,
    val currency: CurrencyType,
    val startOfWeek: StartOfWeek
)
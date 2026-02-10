package com.jorgelobo.koobe.domain.settings.currency

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import javax.inject.Inject

class SetCurrencyUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    suspend operator fun invoke(currency: CurrencyType) {
        preferences.setCurrency(currency)
    }
}